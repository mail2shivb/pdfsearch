package com.example.pdfsearch.model;

import lombok.Data;
import java.util.List;

@Data
public class PdfResponse {
    private List<String> pagesMarkdown;
    private String requestedBlockText;
}