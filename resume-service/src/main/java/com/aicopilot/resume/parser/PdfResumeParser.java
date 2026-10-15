package com.aicopilot.resume.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PdfResumeParser {

    private static final Logger log = LoggerFactory.getLogger(PdfResumeParser.class);

    public String extractText(byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("PDF byte array is null or empty");
        }
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document);
        }
    }

    public String extractText(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("PDF file does not exist: " + (file != null ? file.getAbsolutePath() : "null"));
        }
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document);
        }
    }

    public String extractText(InputStream inputStream) throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        return extractText(bytes);
    }
}
