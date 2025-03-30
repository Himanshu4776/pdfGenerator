package com.tool.pdfGenerator.service;

import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.Collections;


public class PdfGeneratorServiceTest {

    private PdfGeneratorService pdfGeneratorService;
    private Invoice testInvoice;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        pdfGeneratorService = new PdfGeneratorService();

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
}