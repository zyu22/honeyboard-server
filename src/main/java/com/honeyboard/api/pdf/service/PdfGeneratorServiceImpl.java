package com.honeyboard.api.pdf.service;

import com.honeyboard.api.pdf.model.Document;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public
class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private final SpringTemplateEngine templateEngine;

    @Override
    public byte[] generatePdf(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("문서 정보가 없습니다.");
        }

        try {
            String html = generateHtml(document);
            return generatePdfFromHtml(html);
        } catch (Exception e) {
            log.error("PDF 생성 실패 - 문서 ID: {}, 오류: {}", document.getId(), e.getMessage());
            throw new RuntimeException("PDF 생성에 실패했습니다.", e);
        }
    }


    @Override
    public byte[] generatePdfFromMultipleDocuments(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            throw new IllegalArgumentException("문서 목록이 비어있습니다.");
        }

        try {
            String html = generateHtmlForMultipleDocuments(documents);
            return generatePdfFromHtml(html);
        } catch (Exception e) {
            log.error("다중 문서 PDF 생성 실패 - 문서 수: {}, 오류: {}", documents.size(), e.getMessage());
            throw new RuntimeException("다중 문서 PDF 생성에 실패했습니다.", e);
        }
    }

    private byte[] generatePdfFromHtml(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, "/");
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF 파일 생성 중 오류가 발생했습니다.", e);
        }
    }

    private String generateHtml(Document document) {
        try {
            Context context = new Context();
            context.setVariable("document", document);
            return templateEngine.process("pdf/document-template", context);
        } catch (Exception e) {
            throw new RuntimeException("HTML 템플릿 처리 중 오류가 발생했습니다.", e);
        }
    }

    private String generateHtmlForMultipleDocuments(List<Document> documents) {
        try {
            Context context = new Context();
            context.setVariable("documents", documents);
            return templateEngine.process("pdf/multiple-documents-template", context);
        } catch (Exception e) {
            throw new RuntimeException("다중 문서 HTML 템플릿 처리 중 오류가 발생했습니다.", e);
        }
    }
}