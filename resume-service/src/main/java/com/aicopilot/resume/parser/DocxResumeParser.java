package com.aicopilot.resume.parser;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocxResumeParser {

    private static final Logger log = LoggerFactory.getLogger(DocxResumeParser.class);

    public String extractText(byte[] docxBytes) throws IOException {
        if (docxBytes == null || docxBytes.length == 0) {
            throw new IllegalArgumentException("DOCX byte array is null or empty");
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(docxBytes);
             XWPFDocument document = new XWPFDocument(bais);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    public String extractText(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("DOCX file does not exist: " + (file != null ? file.getAbsolutePath() : "null"));
        }
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    public String extractText(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }
}
