package com.tool.pdfGenerator.service;

import static org.junit.jupiter.api.Assertions.*;

import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.model.Item;
import com.tool.pdfGenerator.util.HashUtil;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private PdfGeneratorService pdfGeneratorService;

    @InjectMocks
    private InvoiceService invoiceService;

    @TempDir
    Path tempDir;

    private Invoice testInvoice;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        // Set the PDF storage path to our temp directory for testing
        invoiceService.setPdfStoragePath(tempDir.toString() + "/");

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
    void generateInvoice_NewInvoice_ShouldGeneratePdf() throws IOException, DocumentException {
        String hash = "testhash123";

        try (MockedStatic<HashUtil> hashUtilMock = mockStatic(HashUtil.class)) {
            hashUtilMock.when(() -> HashUtil.generateHash(any(Invoice.class))).thenReturn(hash);

            // Create test content
            byte[] testContent = "Test PDF Content".getBytes();
            Path pdfPath = tempDir.resolve(hash + ".pdf");
            Files.write(pdfPath, testContent);

            doNothing().when(pdfGeneratorService).generateInvoicePdf(any(Invoice.class), anyString());

            byte[] result = invoiceService.generateInvoice(testInvoice);

            assertArrayEquals(testContent, result);
            verify(pdfGeneratorService).generateInvoicePdf(eq(testInvoice), anyString());
        }
    }

    @Test
    void generateInvoice_ExistingInvoice_ShouldRetrieveFromStorage() throws IOException, DocumentException {
        String hash = "testhash123";
        byte[] testContent = "Test PDF Content".getBytes();

        try (MockedStatic<HashUtil> hashUtilMock = mockStatic(HashUtil.class)) {
            hashUtilMock.when(() -> HashUtil.generateHash(any(Invoice.class))).thenReturn(hash);

            // Create the file before test
            Path pdfPath = tempDir.resolve(hash + ".pdf");
            Files.write(pdfPath, testContent);

            byte[] result = invoiceService.generateInvoice(testInvoice);

            verify(pdfGeneratorService, never()).generateInvoicePdf(any(Invoice.class), anyString());
            assertArrayEquals(testContent, result);
        }
    }

    @Test
    void getInvoicePdf_ExistingPdf_ShouldReturnContent() throws IOException {
        String hash = "testhash123";
        byte[] testContent = "Test PDF Content".getBytes();

        // Create the file before test
        Path pdfPath = tempDir.resolve(hash + ".pdf");
        Files.write(pdfPath, testContent);

        byte[] result = invoiceService.getInvoicePdf(hash);

        assertArrayEquals(testContent, result);
    }

    @Test
    void getInvoicePdf_NonExistingPdf_ShouldThrowException() {
        String hash = "nonexistinghash";

        assertThrows(IOException.class, () -> invoiceService.getInvoicePdf(hash));
    }

    @Test
    void invoicePdfExists_ExistingPdf_ShouldReturnTrue() throws IOException {
        String hash = "testhash123";

        // Create the file before test
        Path pdfPath = tempDir.resolve(hash + ".pdf");
        Files.write(pdfPath, "Test PDF Content".getBytes());

        boolean result = invoiceService.invoicePdfExists(hash);

        assertTrue(result);
    }

    @Test
    void invoicePdfExists_NonExistingPdf_ShouldReturnFalse() {
        String hash = "nonexistinghash";

        boolean result = invoiceService.invoicePdfExists(hash);

        assertFalse(result);
    }
}