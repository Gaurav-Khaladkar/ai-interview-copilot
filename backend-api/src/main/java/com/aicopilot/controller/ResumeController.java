package com.aicopilot.controller;

import com.aicopilot.dto.ApiResponse;
import com.aicopilot.dto.ResumeUploadResponse;
import com.aicopilot.model.Resume;
import com.aicopilot.security.SecurityUtils;
import com.aicopilot.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@Tag(name = "Resume Management", description = "PDF/DOCX upload, parsing, skills discovery and suggestions")
public class ResumeController {

    private final ResumeService resumeService;
    private final SecurityUtils securityUtils;

    public ResumeController(ResumeService resumeService, SecurityUtils securityUtils) {
        this.resumeService = resumeService;
        this.securityUtils = securityUtils;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload and parse candidate PDF or DOCX resume")
    public ResponseEntity<ApiResponse<ResumeUploadResponse>> uploadResume(@RequestParam("file") MultipartFile file) throws IOException {
        String userId = securityUtils.getCurrentUserId();
        String originalFilename = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        ResumeUploadResponse response = resumeService.uploadAndProcess(userId, originalFilename, bytes);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Resume parsed successfully.", response));
    }

    @GetMapping
    @Operation(summary = "List candidate uploaded resumes")
    public ResponseEntity<ApiResponse<List<ResumeUploadResponse>>> listResumes() {
        String userId = securityUtils.getCurrentUserId();
        List<ResumeUploadResponse> resumes = resumeService.getUserResumes(userId);
        return ResponseEntity.ok(ApiResponse.ok(resumes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get parsed resume details by ID")
    public ResponseEntity<ApiResponse<Resume>> getResume(@PathVariable String id) {
        String userId = securityUtils.getCurrentUserId();
        return resumeService.getResumeById(userId, id)
            .map(r -> ResponseEntity.ok(ApiResponse.ok(r)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Resume not found.")));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete resume and associated extracted data")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable String id) {
        String userId = securityUtils.getCurrentUserId();
        boolean deleted = resumeService.deleteResume(userId, id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.ok("Resume deleted successfully.", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Resume not found."));
        }
    }
}
