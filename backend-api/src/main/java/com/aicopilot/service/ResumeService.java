package com.aicopilot.service;

import com.aicopilot.dto.ResumeUploadResponse;
import com.aicopilot.model.Resume;
import com.aicopilot.model.ResumeSkill;
import com.aicopilot.model.User;
import com.aicopilot.repository.ResumeRepository;
import com.aicopilot.repository.ResumeSkillRepository;
import com.aicopilot.repository.UserRepository;
import com.aicopilot.resume.model.ExtractedResumeData;
import com.aicopilot.resume.model.ExtractedSkill;
import com.aicopilot.resume.service.ResumeIntelligenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeSkillRepository skillRepository;
    private final UserRepository userRepository;
    private final ResumeIntelligenceService intelligenceService = new ResumeIntelligenceService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeService(ResumeRepository resumeRepository, ResumeSkillRepository skillRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResumeUploadResponse uploadAndProcess(String userId, String fileName, byte[] fileBytes) throws IOException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        ExtractedResumeData extracted = intelligenceService.parseAndAnalyze(fileName, fileBytes);

        Resume resume = new Resume(user, fileName, extracted.getFileType(), fileBytes.length, extracted.getRawText());
        resume.setQualityScore(extracted.getQualityScore());
        resume.setSuggestionsJson(objectMapper.writeValueAsString(extracted.getSuggestions()));
        resume = resumeRepository.save(resume);

        List<ResumeUploadResponse.SkillDto> skillDtos = new ArrayList<>();
        for (ExtractedSkill s : extracted.getSkills()) {
            ResumeSkill rSkill = new ResumeSkill(resume, s.getName(), s.getCategory(), s.getProficiency());
            skillRepository.save(rSkill);
            skillDtos.add(new ResumeUploadResponse.SkillDto(s.getName(), s.getCategory(), s.getProficiency(), 1));
        }

        ResumeUploadResponse response = new ResumeUploadResponse();
        response.setResumeId(resume.getId());
        response.setFileName(resume.getFileName());
        response.setFileType(resume.getFileType());
        response.setFileSize(resume.getFileSize());
        response.setQualityScore(extracted.getQualityScore());
        response.setSkills(skillDtos);
        response.setEducation(extracted.getEducation());
        response.setCertifications(extracted.getCertifications());
        response.setEstimatedYearsExperience(extracted.getEstimatedYearsExperience());
        response.setSuggestions(extracted.getSuggestions());
        response.setGeneratedQuestions(extracted.getGeneratedQuestions());

        return response;
    }

    public List<ResumeUploadResponse> getUserResumes(String userId) {
        List<Resume> resumes = resumeRepository.findByUserIdOrderByUploadedAtDesc(userId);
        return resumes.stream().map(r -> {
            ResumeUploadResponse dto = new ResumeUploadResponse();
            dto.setResumeId(r.getId());
            dto.setFileName(r.getFileName());
            dto.setFileType(r.getFileType());
            dto.setFileSize(r.getFileSize());
            dto.setQualityScore(r.getQualityScore());

            List<ResumeSkill> skills = skillRepository.findByResumeId(r.getId());
            dto.setSkills(skills.stream()
                .map(s -> new ResumeUploadResponse.SkillDto(s.getSkillName(), s.getCategory(), s.getProficiencyLevel(), s.getYearsExperience()))
                .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public Optional<Resume> getResumeById(String userId, String resumeId) {
        return resumeRepository.findByIdAndUserId(resumeId, userId);
    }

    @Transactional
    public boolean deleteResume(String userId, String resumeId) {
        Optional<Resume> resumeOpt = resumeRepository.findByIdAndUserId(resumeId, userId);
        if (resumeOpt.isPresent()) {
            resumeRepository.delete(resumeOpt.get());
            return true;
        }
        return false;
    }
}
