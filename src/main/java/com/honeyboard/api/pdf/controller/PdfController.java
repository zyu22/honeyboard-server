package com.honeyboard.api.pdf.controller;

import com.honeyboard.api.pdf.model.Document;
import com.honeyboard.api.pdf.model.DocumentType;
import com.honeyboard.api.pdf.service.PdfGeneratorService;
import com.honeyboard.api.project.finale.service.FinaleProjectBoardService;
import com.honeyboard.api.project.track.service.TrackProjectBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PdfController {
//    private final PdfGeneratorService pdfGeneratorService;
//    private final FinaleProjectBoardService finaleProjectBoardService;
//    private final TrackProjectBoardService trackProjectBoardService;
//
//    private DocumentType validateAndGetDocumentType(String type) {
//        try {
//            return DocumentType.valueOf(type.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("지원하지 않는 문서 타입입니다: " + type);
//        }
//    }
//
//    @GetMapping("/{type}/{id}")
//    public ResponseEntity<byte[]> downloadPdf(
//            @PathVariable String type,
//            @PathVariable Integer id) throws IOException {
//
//        DocumentType documentType = validateAndGetDocumentType(type);
//        Document document;
//
//        if (documentType == DocumentType.FINALE) {
//            FinaleProjectBoard board = finaleProjectBoardService.getFinaleBoard(id);
//            document = Document.fromFinaleProject(board);
//        } else if (documentType == DocumentType.TRACK) {
//            TrackProjectBoard board = trackProjectBoardService.getBoard(id);
//            document = Document.fromTrackProject(board);
//        } else {
//            throw new IllegalArgumentException("Invalid document type");
//        }
//
//        byte[] pdfContent = pdfGeneratorService.generatePdf(document);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("filename", document.getTitle() + ".pdf");
//
//        String encodedFilename = URLEncoder.encode(document.getTitle() + ".pdf", StandardCharsets.UTF_8);
//        headers.setContentDisposition(ContentDisposition.builder("attachment")
//                .filename(encodedFilename, StandardCharsets.UTF_8)
//                .build());
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .body(pdfContent);
//
//    }
//
//    @GetMapping("/all/{type}/{id}")
//    public ResponseEntity<byte[]> downloadAllPdf(
//            @PathVariable String type,
//            @PathVariable Integer id) throws IOException {
//        List<Document> documents;
//        String fileName;
//
//        DocumentType documentType = validateAndGetDocumentType(type);
//        Document document;
//
//        if (documentType == DocumentType.FINALE) {
//            List<FinaleProjectBoard> boards = finaleProjectBoardService.getAllFinaleBoards(id);
//            documents = boards.stream()
//                    .map(Document::fromFinaleProject)
//                    .collect(Collectors.toList());
//            fileName = "최종프로젝트_전체목록";
//        } else if (documentType == DocumentType.TRACK) {
//            List<TrackProjectBoard> boards = trackProjectBoardService.getAllBoard(id);
//            documents = boards.stream()
//                    .map(Document::fromTrackProject)
//                    .collect(Collectors.toList());
//            fileName = "트랙프로젝트_전체목록";
//        } else {
//            throw new IllegalArgumentException("Invalid document type");
//        }
//
//        byte[] pdfContent = pdfGeneratorService.generatePdfFromMultipleDocuments(documents);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//
//        String encodedFilename = URLEncoder.encode(fileName + ".pdf", StandardCharsets.UTF_8);
//        headers.setContentDisposition(ContentDisposition.builder("attachment")
//                .filename(encodedFilename, StandardCharsets.UTF_8)
//                .build());
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .body(pdfContent);
//    }

}

