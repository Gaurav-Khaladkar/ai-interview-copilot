package com.aicopilot.resume.model;

import java.util.ArrayList;
import java.util.List;

public class ExtractedResumeData {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String candidateName;
    private String candidateEmail;
    private String candidatePhone;
    private String rawText;
    private List<ExtractedSkill> skills = new ArrayList<>();
    private List<String> education = new ArrayList<>();
    private List<String> certifications = new ArrayList<>();
    private List<String> experienceSnippets = new ArrayList<>();
    private int estimatedYearsExperience = 1;
    private int qualityScore = 0;
    private List<String> suggestions = new ArrayList<>();
    private List<String> generatedQuestions = new ArrayList<>();

    public ExtractedResumeData() {}

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }

    public String getCandidatePhone() { return candidatePhone; }
    public void setCandidatePhone(String candidatePhone) { this.candidatePhone = candidatePhone; }

    public String getRawText() { return rawText; }
    public void setRawText(String rawText) { this.rawText = rawText; }

    public List<ExtractedSkill> getSkills() { return skills; }
    public void setSkills(List<ExtractedSkill> skills) { this.skills = skills; }

    public List<String> getEducation() { return education; }
    public void setEducation(List<String> education) { this.education = education; }

    public List<String> getCertifications() { return certifications; }
    public void setCertifications(List<String> certifications) { this.certifications = certifications; }

    public List<String> getExperienceSnippets() { return experienceSnippets; }
    public void setExperienceSnippets(List<String> experienceSnippets) { this.experienceSnippets = experienceSnippets; }

    public int getEstimatedYearsExperience() { return estimatedYearsExperience; }
    public void setEstimatedYearsExperience(int estimatedYearsExperience) { this.estimatedYearsExperience = estimatedYearsExperience; }

    public int getQualityScore() { return qualityScore; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public List<String> getGeneratedQuestions() { return generatedQuestions; }
    public void setGeneratedQuestions(List<String> generatedQuestions) { this.generatedQuestions = generatedQuestions; }
}
