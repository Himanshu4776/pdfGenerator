package com.tool.pdfGenerator.controller;

import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.service.InvoiceService;
import com.tool.pdfGenerator.util.HashUtil;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateInvoice(@Valid @RequestBody Invoice invoice) {
        try {
            byte[] pdfBytes = invoiceService.generateInvoice(invoice);
            String invoiceHash = HashUtil.generateHash(invoice);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");
            headers.set("Invoice-Hash", invoiceHash);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException | DocumentException e) {
            log.error("Error generating invoice PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable String hash) {
        try {
            if (!invoiceService.invoicePdfExists(hash)) {
                return ResponseEntity.notFound().build();
            }

            byte[] pdfBytes = invoiceService.getInvoicePdf(hash);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error downloading invoice PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}