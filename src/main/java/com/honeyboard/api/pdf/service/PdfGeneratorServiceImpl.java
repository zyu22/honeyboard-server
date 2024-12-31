package com.honeyboard.api.pdf.service;

import com.honeyboard.api.pdf.model.Document;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public
class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final SpringTemplateEngine templateEngine;

    @Override
    public byte[] generatePdf(Document document) throws IOException {
        String html = generateHtml(document);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, "/");
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        }
    }

    @Override
    public byte[] generatePdfFromMultipleDocuments(List<Document> documents) throws IOException {
        String html = generateHtmlForMultipleDocuments(documents);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, "/");
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        }
    }

    private String generateHtml(Document document) {
        Context context = new Context();
        context.setVariable("document", document);
        return templateEngine.process("pdf/document-template", context);
    }

    private String generateHtmlForMultipleDocuments(List<Document> documents) {
        Context context = new Context();
        context.setVariable("documents", documents);
        return templateEngine.process("pdf/multiple-documents-template", context);
    }
}