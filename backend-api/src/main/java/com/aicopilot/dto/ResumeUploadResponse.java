package com.aicopilot.dto;

import java.util.ArrayList;
import java.util.List;

public class ResumeUploadResponse {
    private String resumeId;
    private String fileName;
    private String fileType;
    private long fileSize;
    private int qualityScore;
    private List<SkillDto> skills = new ArrayList<>();
    private List<String> education = new ArrayList<>();
    private List<String> certifications = new ArrayList<>();
    private int estimatedYearsExperience;
    private List<String> suggestions = new ArrayList<>();
    private List<String> generatedQuestions = new ArrayList<>();

    public ResumeUploadResponse() {}

    public String getResumeId() { return resumeId; }
    public void setResumeId(String resumeId) { this.resumeId = resumeId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public int getQualityScore() { return qualityScore; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }

    public List<SkillDto> getSkills() { return skills; }
    public void setSkills(List<SkillDto> skills) { this.skills = skills; }

    public List<String> getEducation() { return education; }
    public void setEducation(List<String> education) { this.education = education; }

    public List<String> getCertifications() { return certifications; }
    public void setCertifications(List<String> certifications) { this.certifications = certifications; }

    public int getEstimatedYearsExperience() { return estimatedYearsExperience; }
    public void setEstimatedYearsExperience(int estimatedYearsExperience) { this.estimatedYearsExperience = estimatedYearsExperience; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public List<String> getGeneratedQuestions() { return generatedQuestions; }
    public void setGeneratedQuestions(List<String> generatedQuestions) { this.generatedQuestions = generatedQuestions; }

    public static class SkillDto {
        private String name;
        private String category;
        private String proficiency;
        private int yearsExperience;

        public SkillDto() {}

        public SkillDto(String name, String category, String proficiency, int yearsExperience) {
            this.name = name;
            this.category = category;
            this.proficiency = proficiency;
            this.yearsExperience = yearsExperience;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getProficiency() { return proficiency; }
        public void setProficiency(String proficiency) { this.proficiency = proficiency; }

        public int getYearsExperience() { return yearsExperience; }
        public void setYearsExperience(int yearsExperience) { this.yearsExperience = yearsExperience; }
    }
}
