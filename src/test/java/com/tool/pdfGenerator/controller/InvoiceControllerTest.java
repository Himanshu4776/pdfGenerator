package com.tool.pdfGenerator.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.model.Item;
import com.tool.pdfGenerator.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    private Invoice testInvoice;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        Item item = new Item("Product 1", "12 Nos", 123.00, 1476.00);
        testInvoice = new Invoice(
                "XYZ Pvt. Ltd.",
                "29AABBCCDD121ZD",
                "New Delhi, India",
                "Vedant Computers",
                "29AABBCCDD131ZD",
                "New Delhi, India",
                Collections.singletonList(item)
        );
    }

    @Test
    void generateInvoice_ShouldReturnPdf() throws Exception {
        byte[] samplePdf = "Sample PDF Content".getBytes();
        String hash = "samplehash123";

        when(invoiceService.generateInvoice(any(Invoice.class))).thenReturn(samplePdf);

        ResponseEntity<byte[]> result = invoiceController.generateInvoice(testInvoice);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(true, result.getHeaders().containsKey("Invoice-Hash"));
        assertEquals(result.getHeaders().getContentType(), MediaType.APPLICATION_PDF);
    }

    @Test
    void downloadInvoice_ExistingHash_ShouldReturnPdf() throws Exception {
        String hash = "samplehash123";
        byte[] samplePdf = "Sample PDF Content".getBytes();

        when(invoiceService.invoicePdfExists(hash)).thenReturn(true);
        when(invoiceService.getInvoicePdf(hash)).thenReturn(samplePdf);

        ResponseEntity<byte[]> result = invoiceController.downloadInvoice(hash);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(result.getHeaders().getContentType(), MediaType.APPLICATION_PDF);
    }

    @Test
    void downloadInvoice_NonExistingHash_ShouldReturnNotFound() throws Exception {
        String hash = "nonexistinghash";
        when(invoiceService.invoicePdfExists(hash)).thenReturn(false);

        ResponseEntity<byte[]> result = invoiceController.downloadInvoice(hash);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}