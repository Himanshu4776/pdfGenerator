package com.tool.pdfGenerator.service;

import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.util.HashUtil;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final PdfGeneratorService pdfGeneratorService;
    private static final String PDF_STORAGE_PATH = "pdf-storage/";

    public byte[] generateInvoice(Invoice invoice) throws IOException, DocumentException {
        String invoiceHash = HashUtil.generateHash(invoice);
        String pdfFilePath = PDF_STORAGE_PATH + invoiceHash + ".pdf";
        File pdfFile = new File(pdfFilePath);

        // Check if PDF already exists
        if (pdfFile.exists()) {
            log.info("PDF already exists for this invoice data, retrieving from storage");
            return Files.readAllBytes(Paths.get(pdfFilePath));
        }

        // Generate new PDF
        log.info("Generating new PDF for invoice");
        pdfGeneratorService.generateInvoicePdf(invoice, pdfFilePath);
        return Files.readAllBytes(Paths.get(pdfFilePath));
    }

    public byte[] getInvoicePdf(String hash) throws IOException {
        String pdfFilePath = PDF_STORAGE_PATH + hash + ".pdf";
        Path path = Paths.get(pdfFilePath);

        if (!Files.exists(path)) {
            throw new IOException("PDF file not found for hash: " + hash);
        }

        return Files.readAllBytes(path);
    }

    public boolean invoicePdfExists(String hash) {
        String pdfFilePath = PDF_STORAGE_PATH + hash + ".pdf";
        return new File(pdfFilePath).exists();
    }
}