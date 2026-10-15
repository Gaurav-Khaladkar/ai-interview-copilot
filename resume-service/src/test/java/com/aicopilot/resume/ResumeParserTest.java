package com.aicopilot.resume;

import com.aicopilot.resume.model.ExtractedResumeData;
import com.aicopilot.resume.model.ExtractedSkill;
import com.aicopilot.resume.service.ResumeIntelligenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResumeParserTest {

    private ResumeIntelligenceService resumeService;

    @BeforeEach
    void setUp() {
        resumeService = new ResumeIntelligenceService();
    }

    @Test
    @DisplayName("Extract technical skills, contacts and generate questions without hallucination")
    void testResumeExtractionAndScoring() {
        String sampleText =
            "Alex Morgan\n" +
            "alex.morgan@techcorp.com | 123-456-7890\n" +
            "Professional Summary: 5 years of experience in Java, Spring Boot, PostgreSQL, Docker, and Kubernetes.\n" +
            "Technical Skills: Java, Spring Boot, Hibernate, SQL, PostgreSQL, Docker, Kubernetes, AWS, Microservices, Git, JUnit.\n" +
            "Projects: Architected microservice transaction system handling 15,000 req/sec, reducing latency by 45%.\n" +
            "Education: Bachelor of Science in Computer Science, 2019.\n" +
            "Certifications: AWS Certified Solutions Architect.";

        ExtractedResumeData data = resumeService.analyzeText("Alex_Morgan_Resume.pdf", "application/pdf", 1024 * 50, sampleText);

        assertNotNull(data);
        assertEquals("Alex Morgan", data.getCandidateName());
        assertEquals("alex.morgan@techcorp.com", data.getCandidateEmail());
        assertEquals("123-456-7890", data.getCandidatePhone());

        // Verify skills
        List<ExtractedSkill> skills = data.getSkills();
        assertFalse(skills.isEmpty());
        assertTrue(skills.stream().anyMatch(s -> s.getName().equalsIgnoreCase("Java")));
        assertTrue(skills.stream().anyMatch(s -> s.getName().equalsIgnoreCase("Spring Boot")));
        assertTrue(skills.stream().anyMatch(s -> s.getName().equalsIgnoreCase("Docker")));

        // Verify scoring
        assertTrue(data.getQualityScore() >= 70 && data.getQualityScore() <= 100);

        // Verify resume-based question generation
        assertFalse(data.getGeneratedQuestions().isEmpty());
        assertTrue(data.getGeneratedQuestions().stream().anyMatch(q -> q.contains("Java") || q.contains("Spring Boot") || q.contains("Docker")));
    }

    @Test
    @DisplayName("Rejects files exceeding size limit or invalid extensions")
    void testFileValidationRules() {
        assertThrows(IllegalArgumentException.class, () -> {
            resumeService.parseAndAnalyze("resume.exe", new byte[100]);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            byte[] oversized = new byte[11 * 1024 * 1024]; // 11MB
            resumeService.parseAndAnalyze("large_resume.pdf", oversized);
        });
    }
}
