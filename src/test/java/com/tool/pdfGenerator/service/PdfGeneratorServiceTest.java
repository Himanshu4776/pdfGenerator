package com.tool.pdfGenerator.service;

import com.itextpdf.text.DocumentException;
import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.model.Item;
import com.tool.pdfGenerator.util.HashUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.file.Path;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PdfGeneratorServiceTest {
    @BeforeEach
    void setup() throws IOException {
        File directory = new File("test-pdf-storage");
        if (!directory.exists()) {
            directory.mkdir();
        }

        Path folderPath = Paths.get("test-pdf-storage/");
        FileUtils.cleanDirectory(folderPath.toFile());
    }

    @AfterEach
    public void tearDown() {
        Path folderPath = Paths.get("test-pdf-storage/");
        try {
            FileUtils.deleteDirectory(folderPath.toFile());
        } catch (IOException e) {
            // Handle exception
            System.out.println("Error deleting directory: " + e.getMessage());
        }
    }

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private PdfGeneratorService pdfGeneratorService;

    @Test
    void generateInvoicePdf_InvoiceAndPathProvided_ReturnsIfFileExists() throws IOException, DocumentException {
        // Arrange
        Invoice invoice = new Invoice();
        invoice.setSeller("Seller");
        invoice.setSellerGstin("GSTIN");
        invoice.setSellerAddress("Address");
        invoice.setBuyer("Buyer");
        invoice.setBuyerGstin("GSTIN");
        invoice.setBuyerAddress("Address");
        invoice.setItems(new ArrayList<>());

        String hash = HashUtil.generateHash(invoice);
        String path = "test-pdf-storage/" + hash + ".pdf";
        when(invoiceService.generateInvoice(any(Invoice.class))).thenReturn("pdfBytes".getBytes());

        pdfGeneratorService.generateInvoicePdf(invoice, path);

        File file = new File(path);
        assertEquals(true, file.exists());
    }

    @Test
    void generateInvoicePdf_InvoiceNull_ThrowsNullPointerException() {
        // Arrange
        Invoice invoice = null;
        String path = "path/to/invoice.pdf";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> pdfGeneratorService.generateInvoicePdf(invoice, path));
    }

    @Test
    void generateInvoicePdf_PathNull_ThrowsNullPointerException() {
        // Arrange
        Invoice invoice = new Invoice();
        String path = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> pdfGeneratorService.generateInvoicePdf(invoice, path));
    }
}