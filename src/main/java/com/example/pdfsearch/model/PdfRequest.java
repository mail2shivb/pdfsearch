package com.example.pdfsearch.model;

import lombok.Data;

@Data
public class PdfRequest {
    private String pdfFileName;
    private String blockText;
}