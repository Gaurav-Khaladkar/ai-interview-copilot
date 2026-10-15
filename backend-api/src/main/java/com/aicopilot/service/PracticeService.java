package com.aicopilot.service;

import com.aicopilot.ai.model.PracticeEvaluationRequest;
import com.aicopilot.ai.model.PracticeEvaluationResponse;
import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.dto.*;
import com.aicopilot.model.*;
import com.aicopilot.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PracticeService {

    private final PracticeSessionRepository sessionRepository;
    private final PracticeQuestionRepository questionRepository;
    private final PracticeAnswerRepository answerRepository;
    private final PracticeReportRepository reportRepository;
    private final ResumeSkillRepository skillRepository;
    private final UserRepository userRepository;
    private final AiServiceRouter aiRouter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PracticeService(PracticeSessionRepository sessionRepository,
                           PracticeQuestionRepository questionRepository,
                           PracticeAnswerRepository answerRepository,
                           PracticeReportRepository reportRepository,
                           ResumeSkillRepository skillRepository,
                           UserRepository userRepository,
                           AiServiceRouter aiRouter) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.reportRepository = reportRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.aiRouter = aiRouter;
    }

    @Transactional
    public PracticeSessionResponseDto createSession(String userId, PracticeSessionRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        PracticeSession session = new PracticeSession(
            user, request.getJobRole(), request.getTopic(), request.getDifficulty(), request.getLanguage(), request.getDurationMinutes()
        );
        session = sessionRepository.save(session);

        // Fetch skills from resume if provided
        List<String> resumeSkills = new ArrayList<>();
        if (request.getResumeId() != null && !request.getResumeId().isBlank()) {
            List<ResumeSkill> skills = skillRepository.findByResumeId(request.getResumeId());
            resumeSkills = skills.stream().map(ResumeSkill::getSkillName).collect(Collectors.toList());
        }

        // Generate 5 questions via AI Service
        List<String> questionsText = aiRouter.generateQuestions(
            resumeSkills, request.getTopic(), request.getDifficulty(), 5
        );

        if (questionsText == null || questionsText.isEmpty()) {
            questionsText = Arrays.asList(
                "Explain the internal architecture of your core technical stack and its main scaling bottlenecks.",
                "How do you design for high concurrency and avoid race conditions in multi-threaded workflows?",
                "Describe a situation where a database query degraded production performance and how you resolved it.",
                "What strategies do you employ for resilience and circuit-breaking in distributed microservices?",
                "How do you ensure comprehensive automated testing (unit, integration, and load) across deployments?"
            );
        }

        PracticeQuestion firstQ = null;
        for (int i = 0; i < questionsText.size(); i++) {
            PracticeQuestion pq = new PracticeQuestion(session, i + 1, questionsText.get(i), i == 0 ? "BEHAVIORAL" : "TECHNICAL");
            pq.setDifficulty(request.getDifficulty());
            pq = questionRepository.save(pq);
            if (i == 0) {
                firstQ = pq;
            }
        }

        PracticeSessionResponseDto response = new PracticeSessionResponseDto();
        response.setSessionId(session.getId());
        response.setJobRole(session.getJobRole());
        response.setTopic(session.getTopic());
        response.setDifficulty(session.getDifficulty());
        response.setLanguage(session.getLanguage());
        response.setStatus(session.getStatus());
        response.setTotalQuestions(questionsText.size());

        if (firstQ != null) {
            response.setFirstQuestion(new PracticeSessionResponseDto.QuestionDto(
                firstQ.getId(), firstQ.getQuestionOrder(), firstQ.getQuestionText(), firstQ.getQuestionType()
            ));
        }

        return response;
    }

    @Transactional
    public PracticeFeedbackDto submitAnswer(String userId, String sessionId, PracticeAnswerRequest request) {
        PracticeSession session = sessionRepository.findByIdAndUserId(sessionId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        PracticeQuestion currentQuestion = questionRepository.findById(request.getQuestionId())
            .orElseThrow(() -> new IllegalArgumentException("Question not found: " + request.getQuestionId()));

        // Call AI evaluation
        PracticeEvaluationRequest evalReq = new PracticeEvaluationRequest(
            currentQuestion.getQuestionText(),
            request.getUserAnswer(),
            session.getJobRole(),
            session.getTopic(),
            session.getDifficulty(),
            session.getLanguage()
        );

        PracticeEvaluationResponse evalRes = aiRouter.evaluateAnswer(evalReq);

        PracticeAnswer answer = new PracticeAnswer(currentQuestion, request.getUserAnswer(), request.getInputMode());
        answer.setTranscriptionText(request.getTranscriptionText());
        answer.setScoreAccuracy(evalRes.getScoreAccuracy());
        answer.setScoreCompleteness(evalRes.getScoreCompleteness());
        answer.setScoreCommunication(evalRes.getScoreCommunication());
        answer.setAiFeedback(evalRes.getFeedback());
        answer.setIdealAnswerSample(evalRes.getIdealAnswerSample());
        answerRepository.save(answer);

        PracticeFeedbackDto feedback = new PracticeFeedbackDto();
        feedback.setScoreAccuracy(evalRes.getScoreAccuracy());
        feedback.setScoreCompleteness(evalRes.getScoreCompleteness());
        feedback.setScoreCommunication(evalRes.getScoreCommunication());
        feedback.setOverallScore(evalRes.getOverallScore());
        feedback.setFeedback(evalRes.getFeedback());
        feedback.setIdealAnswerSample(evalRes.getIdealAnswerSample());
        feedback.setSuggestions(evalRes.getSuggestions());

        // Find next question
        Optional<PracticeQuestion> nextQOpt = questionRepository.findBySessionIdAndQuestionOrder(
            sessionId, currentQuestion.getQuestionOrder() + 1
        );

        if (nextQOpt.isPresent()) {
            PracticeQuestion nextQ = nextQOpt.get();
            feedback.setNextQuestion(new PracticeSessionResponseDto.QuestionDto(
                nextQ.getId(), nextQ.getQuestionOrder(), nextQ.getQuestionText(), nextQ.getQuestionType()
            ));
            feedback.setSessionComplete(false);
        } else {
            // All questions answered, conclude session and generate report
            session.setStatus("COMPLETED");
            session.setCompletedAt(Instant.now());
            sessionRepository.save(session);
            generateReportInternal(session);
            feedback.setSessionComplete(true);
        }

        return feedback;
    }

    private void generateReportInternal(PracticeSession session) {
        List<PracticeQuestion> questions = questionRepository.findBySessionIdOrderByQuestionOrderAsc(session.getId());
        int totalAccuracy = 0;
        int totalCompleteness = 0;
        int totalCommunication = 0;
        int answeredCount = 0;

        for (PracticeQuestion q : questions) {
            Optional<PracticeAnswer> ansOpt = answerRepository.findByQuestionId(q.getId());
            if (ansOpt.isPresent()) {
                PracticeAnswer a = ansOpt.get();
                totalAccuracy += a.getScoreAccuracy();
                totalCompleteness += a.getScoreCompleteness();
                totalCommunication += a.getScoreCommunication();
                answeredCount++;
            }
        }

        int count = answeredCount > 0 ? answeredCount : 1;
        int avgAcc = totalAccuracy / count;
        int avgComp = totalCompleteness / count;
        int avgComm = totalCommunication / count;
        int overall = (avgAcc + avgComp + avgComm) / 3;

        PracticeReport report = new PracticeReport(session, overall, (avgAcc + avgComp) / 2, avgComm);
        try {
            report.setStrengthsJson(objectMapper.writeValueAsString(Arrays.asList(
                "Strong conceptual grasp of " + session.getTopic(),
                "Clear, structured communication with appropriate technical terminology"
            )));
            report.setWeaknessesJson(objectMapper.writeValueAsString(Arrays.asList(
                "Consider incorporating more concrete architectural trade-offs",
                "Deepen explanation of edge cases under high distributed load"
            )));
            report.setRecommendedTopicsJson(objectMapper.writeValueAsString(Arrays.asList(
                "Distributed Locking & Consensus (Raft / Paxos)",
                "Reactive Streams & Backpressure Management"
            )));
        } catch (Exception ignored) {}

        reportRepository.save(report);
    }

    public PracticeReportDto getReport(String userId, String sessionId) {
        PracticeSession session = sessionRepository.findByIdAndUserId(sessionId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        PracticeReport report = reportRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Report not yet generated for session: " + sessionId));

        PracticeReportDto dto = new PracticeReportDto();
        dto.setReportId(report.getId());
        dto.setSessionId(session.getId());
        dto.setJobRole(session.getJobRole());
        dto.setTopic(session.getTopic());
        dto.setOverallScore(report.getOverallScore());
        dto.setTechnicalScore(report.getTechnicalScore());
        dto.setCommunicationScore(report.getCommunicationScore());
        dto.setCompletedAt(session.getCompletedAt() != null ? session.getCompletedAt() : session.getStartedAt());

        try {
            if (report.getStrengthsJson() != null) {
                dto.setStrengths(objectMapper.readValue(report.getStrengthsJson(), List.class));
            }
            if (report.getWeaknessesJson() != null) {
                dto.setWeaknesses(objectMapper.readValue(report.getWeaknessesJson(), List.class));
            }
            if (report.getRecommendedTopicsJson() != null) {
                dto.setRecommendedTopics(objectMapper.readValue(report.getRecommendedTopicsJson(), List.class));
            }
        } catch (Exception ignored) {}

        return dto;
    }

    public List<PracticeSessionResponseDto> getUserHistory(String userId) {
        List<PracticeSession> sessions = sessionRepository.findByUserIdOrderByStartedAtDesc(userId);
        return sessions.stream().map(s -> {
            PracticeSessionResponseDto dto = new PracticeSessionResponseDto();
            dto.setSessionId(s.getId());
            dto.setJobRole(s.getJobRole());
            dto.setTopic(s.getTopic());
            dto.setDifficulty(s.getDifficulty());
            dto.setLanguage(s.getLanguage());
            dto.setStatus(s.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
}
