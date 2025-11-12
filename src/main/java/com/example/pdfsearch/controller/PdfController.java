package com.example.pdfsearch.controller;

import com.example.pdfsearch.model.PdfRequest;
import com.example.pdfsearch.model.PdfResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
@Slf4j
public class PdfController {

    @PostMapping("/md-pages")
    public ResponseEntity<PdfResponse> getPdfPagesAsMarkdown(@RequestBody PdfRequest request) {
        String resourcePath = "/files/" + request.getPdfFileName();
        List<String> pagesMarkdown = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                log.error("File not found in resources: {}", resourcePath);
                return ResponseEntity.badRequest().body(null);
            }
            try (PDDocument document = PDDocument.load(is)) {
                PDFTextStripper stripper = new PDFTextStripper();
                int numPages = document.getNumberOfPages();

                for (int p = 1; p <= numPages; p++) {
                    stripper.setStartPage(p);
                    stripper.setEndPage(p);
                    String pageText = stripper.getText(document);
                    String markdown = "## Page " + p + "\n\n" + pageText.replaceAll("(\\r\\n|\\n)", "  \n");
                    pagesMarkdown.add(markdown);
                }

                PdfResponse response = new PdfResponse();
                response.setPagesMarkdown(pagesMarkdown);
                response.setRequestedBlockText(request.getBlockText());
                return ResponseEntity.ok(response);

            }
        } catch (Exception e) {
            log.error("Error reading PDF: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}