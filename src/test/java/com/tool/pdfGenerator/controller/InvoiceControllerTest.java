package com.tool.pdfGenerator.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.model.Item;
import com.tool.pdfGenerator.service.InvoiceService;
import com.tool.pdfGenerator.util.HashUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InvoiceService invoiceService;

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

        mockMvc.perform(post("/api/invoices/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testInvoice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().exists("Invoice-Hash"));
    }

    @Test
    void downloadInvoice_ExistingHash_ShouldReturnPdf() throws Exception {
        String hash = "samplehash123";
        byte[] samplePdf = "Sample PDF Content".getBytes();

        when(invoiceService.invoicePdfExists(hash)).thenReturn(true);
        when(invoiceService.getInvoicePdf(hash)).thenReturn(samplePdf);

        mockMvc.perform(get("/api/invoices/download/{hash}", hash))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void downloadInvoice_NonExistingHash_ShouldReturnNotFound() throws Exception {
        String hash = "nonexistinghash";

        when(invoiceService.invoicePdfExists(hash)).thenReturn(false);

        mockMvc.perform(get("/api/invoices/download/{hash}", hash))
                .andExpect(status().isNotFound());
    }
}