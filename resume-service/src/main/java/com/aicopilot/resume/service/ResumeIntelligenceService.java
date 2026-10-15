package com.aicopilot.resume.service;

import com.aicopilot.resume.extractor.SkillDictionary;
import com.aicopilot.resume.model.ExtractedResumeData;
import com.aicopilot.resume.model.ExtractedSkill;
import com.aicopilot.resume.parser.DocxResumeParser;
import com.aicopilot.resume.parser.PdfResumeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeIntelligenceService {

    private static final Logger log = LoggerFactory.getLogger(ResumeIntelligenceService.class);

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private final PdfResumeParser pdfParser = new PdfResumeParser();
    private final DocxResumeParser docxParser = new DocxResumeParser();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\+?\\d{1,3}[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}");
    private static final Pattern YEARS_PATTERN = Pattern.compile("(\\d+)\\+?\\s*(years?|yrs?)\\s*(of)?\\s*(experience|exp)?", Pattern.CASE_INSENSITIVE);

    public ExtractedResumeData parseAndAnalyze(String fileName, byte[] fileBytes) throws IOException {
        validateFile(fileName, fileBytes);

        String rawText;
        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".pdf")) {
            rawText = pdfParser.extractText(fileBytes);
        } else if (lowerName.endsWith(".docx")) {
            rawText = docxParser.extractText(fileBytes);
        } else {
            throw new IllegalArgumentException("Unsupported file type. Please upload a PDF or DOCX file.");
        }

        return analyzeText(fileName, lowerName.endsWith(".pdf") ? "application/pdf" : "application/vnd.openxmlformats-officedocument.wordprocessingml.document", fileBytes.length, rawText);
    }

    public ExtractedResumeData parseAndAnalyze(File file) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }
        byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
        return parseAndAnalyze(file.getName(), bytes);
    }

    public ExtractedResumeData analyzeText(String fileName, String fileType, long fileSize, String rawText) {
        ExtractedResumeData data = new ExtractedResumeData();
        data.setFileName(fileName);
        data.setFileType(fileType);
        data.setFileSize(fileSize);
        data.setRawText(rawText);

        // 1. Extract Candidate Contact Details
        Matcher emailMatcher = EMAIL_PATTERN.matcher(rawText);
        if (emailMatcher.find()) {
            data.setCandidateEmail(emailMatcher.group());
        }

        Matcher phoneMatcher = PHONE_PATTERN.matcher(rawText);
        if (phoneMatcher.find()) {
            data.setCandidatePhone(phoneMatcher.group());
        }

        // Candidate Name heuristic: usually in the first 3 lines
        String[] lines = rawText.split("\\r?\\n");
        for (int i = 0; i < Math.min(5, lines.length); i++) {
            String line = lines[i].trim();
            if (line.length() >= 3 && line.length() <= 40 && !line.contains("@") && !line.matches(".*\\d.*")) {
                data.setCandidateName(line);
                break;
            }
        }
        if (data.getCandidateName() == null || data.getCandidateName().isEmpty()) {
            data.setCandidateName("Candidate");
        }

        // 2. Extract Skills with boundary check and frequency
        List<ExtractedSkill> extractedSkills = new ArrayList<>();
        Map<String, Integer> skillCounts = new HashMap<>();

        for (Map.Entry<String, String> entry : SkillDictionary.SKILL_CATEGORY_MAP.entrySet()) {
            String skill = entry.getKey();
            String category = entry.getValue();

            // Word boundary regex
            String escaped = Pattern.quote(skill);
            Pattern pattern = Pattern.compile("(?i)(?<=^|[^a-zA-Z0-9_+#.-])" + escaped + "(?=[^a-zA-Z0-9_+#.-]|$)");
            Matcher m = pattern.matcher(rawText);
            int count = 0;
            while (m.find()) {
                count++;
            }

            if (count > 0) {
                skillCounts.put(skill, count);
                String proficiency = count >= 4 ? "Advanced" : (count >= 2 ? "Intermediate" : "Foundational");
                extractedSkills.add(new ExtractedSkill(skill, category, proficiency, count));
            }
        }

        // Sort skills by frequency descending
        extractedSkills.sort((a, b) -> Integer.compare(b.getMentionCount(), a.getMentionCount()));
        data.setSkills(extractedSkills);

        // 3. Extract Education Credentials
        List<String> educationList = new ArrayList<>();
        String[] eduKeywords = {"Bachelor", "B.Tech", "B.E.", "Master", "M.Tech", "M.S.", "Computer Science", "Information Technology", "University", "College", "Degree"};
        for (String line : lines) {
            for (String kw : eduKeywords) {
                if (line.toLowerCase().contains(kw.toLowerCase()) && line.length() < 120 && !educationList.contains(line.trim())) {
                    educationList.add(line.trim());
                    break;
                }
            }
        }
        data.setEducation(educationList);

        // 4. Extract Certifications
        List<String> certifications = new ArrayList<>();
        String[] certKeywords = {"Certified", "Certification", "AWS Certified", "Oracle Certified", "CKA", "Azure Certified"};
        for (String line : lines) {
            for (String kw : certKeywords) {
                if (line.toLowerCase().contains(kw.toLowerCase()) && line.length() < 120 && !certifications.contains(line.trim())) {
                    certifications.add(line.trim());
                    break;
                }
            }
        }
        data.setCertifications(certifications);

        // 5. Estimated Years of Experience
        Matcher expMatcher = YEARS_PATTERN.matcher(rawText);
        if (expMatcher.find()) {
            try {
                int exp = Integer.parseInt(expMatcher.group(1));
                data.setEstimatedYearsExperience(Math.min(30, exp));
            } catch (NumberFormatException ignored) {}
        } else if (rawText.toLowerCase().contains("senior")) {
            data.setEstimatedYearsExperience(5);
        } else if (rawText.toLowerCase().contains("lead")) {
            data.setEstimatedYearsExperience(7);
        }

        // 6. Calculate Quality Score (0 - 100)
        int score = 30; // baseline
        if (data.getCandidateEmail() != null) score += 10;
        if (data.getCandidatePhone() != null) score += 10;
        if (!educationList.isEmpty()) score += 10;
        if (!extractedSkills.isEmpty()) {
            score += Math.min(25, extractedSkills.size() * 3);
        }
        if (!certifications.isEmpty()) score += 10;
        if (rawText.matches("(?i).*(optimized|reduced|increased|improved|delivered|achieved|%|ms|latency).*")) {
            score += 15; // Metric-driven bullet points
        }
        data.setQualityScore(Math.min(98, score));

        // 7. Actionable Improvement Suggestions
        List<String> suggestions = new ArrayList<>();
        if (extractedSkills.size() < 6) {
            suggestions.add("Include more categorized technical keywords (e.g. databases, build tools, cloud providers).");
        }
        if (!rawText.matches("(?i).*(%|reduced|increased|improved|scaled).*")) {
            suggestions.add("Add quantified achievements (e.g., 'Reduced API latency by 35% through Redis caching').");
        }
        if (certifications.isEmpty()) {
            suggestions.add("List any relevant cloud or industry certifications (AWS, Spring, CKA) to stand out.");
        }
        if (data.getCandidatePhone() == null) {
            suggestions.add("Ensure your telephone contact information is clearly visible at the top of the resume.");
        }
        suggestions.add("Keep bullet points focused on action verbs and direct architectural contributions.");
        data.setSuggestions(suggestions);

        // 8. Generate Tailored Interview Questions based directly on identified resume skills
        List<String> questions = new ArrayList<>();
        for (ExtractedSkill skill : extractedSkills) {
            String name = skill.getName();
            if (name.equalsIgnoreCase("Java")) {
                questions.add("In your Java projects, how have you handled thread-safety and concurrency pitfalls?");
            } else if (name.equalsIgnoreCase("Spring Boot")) {
                questions.add("Describe the Spring Boot microservice architecture you designed and how you handled configuration management.");
            } else if (name.equalsIgnoreCase("PostgreSQL") || name.equalsIgnoreCase("SQL")) {
                questions.add("How do you analyze slow-running queries and optimize database indexing in PostgreSQL?");
            } else if (name.equalsIgnoreCase("Docker")) {
                questions.add("Explain how you optimized your Docker image size and structured multi-stage container builds.");
            } else if (name.equalsIgnoreCase("Kubernetes")) {
                questions.add("How do you configure readiness and liveness probes in Kubernetes to ensure zero-downtime deployments?");
            } else if (name.equalsIgnoreCase("Kafka")) {
                questions.add("How do you ensure message ordering and handle consumer group rebalancing in Kafka?");
            } else if (name.equalsIgnoreCase("Python")) {
                questions.add("What Python frameworks have you deployed in production, and how did you manage async execution?");
            }
            if (questions.size() >= 5) break;
        }

        if (questions.isEmpty()) {
            questions.add("Walk me through the most technically complex software system you designed and delivered.");
            questions.add("How do you approach automated unit and integration testing in your day-to-day workflow?");
        }

        data.setGeneratedQuestions(questions);
        return data;
    }

    private void validateFile(String fileName, byte[] fileBytes) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty.");
        }
        if (fileBytes == null || fileBytes.length == 0) {
            throw new IllegalArgumentException("File content cannot be empty.");
        }
        if (fileBytes.length > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum permitted limit of 10 MB.");
        }
        String lower = fileName.toLowerCase();
        if (!lower.endsWith(".pdf") && !lower.endsWith(".docx")) {
            throw new IllegalArgumentException("Invalid file format. Only .pdf and .docx documents are supported.");
        }
    }
}
