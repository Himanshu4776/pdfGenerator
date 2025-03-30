package com.tool.pdfGenerator.service;

import com.tool.pdfGenerator.model.Invoice;
import com.tool.pdfGenerator.model.Item;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PdfGeneratorService {

    public void generateInvoicePdf(Invoice invoice, String filePath) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add header with seller and buyer information
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);

        // Seller information
        PdfPCell sellerCell = new PdfPCell();
        sellerCell.setBorder(Rectangle.BOX);
        sellerCell.setPadding(10);
        Paragraph sellerInfo = new Paragraph();
        sellerInfo.add(new Paragraph("Seller:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        sellerInfo.add(new Paragraph(invoice.getSeller(), new Font(Font.FontFamily.HELVETICA, 12)));
        sellerInfo.add(new Paragraph(invoice.getSellerAddress(), new Font(Font.FontFamily.HELVETICA, 12)));
        sellerInfo.add(new Paragraph("GSTIN: " + invoice.getSellerGstin(), new Font(Font.FontFamily.HELVETICA, 12)));
        sellerCell.addElement(sellerInfo);

        // Buyer information
        PdfPCell buyerCell = new PdfPCell();
        buyerCell.setBorder(Rectangle.BOX);
        buyerCell.setPadding(10);
        Paragraph buyerInfo = new Paragraph();
        buyerInfo.add(new Paragraph("Buyer:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        buyerInfo.add(new Paragraph(invoice.getBuyer(), new Font(Font.FontFamily.HELVETICA, 12)));
        buyerInfo.add(new Paragraph(invoice.getBuyerAddress(), new Font(Font.FontFamily.HELVETICA, 12)));
        buyerInfo.add(new Paragraph("GSTIN: " + invoice.getBuyerGstin(), new Font(Font.FontFamily.HELVETICA, 12)));
        buyerCell.addElement(buyerInfo);

        headerTable.addCell(sellerCell);
        headerTable.addCell(buyerCell);
        document.add(headerTable);

        document.add(Chunk.NEWLINE);

        // Add items table
        PdfPTable itemsTable = new PdfPTable(4);
        itemsTable.setWidthPercentage(100);

        // Define table headers
        PdfPCell itemHeader = new PdfPCell(new Phrase("Item", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        PdfPCell quantityHeader = new PdfPCell(new Phrase("Quantity", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        PdfPCell rateHeader = new PdfPCell(new Phrase("Rate", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        PdfPCell amountHeader = new PdfPCell(new Phrase("Amount", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

        itemHeader.setPadding(5);
        quantityHeader.setPadding(5);
        rateHeader.setPadding(5);
        amountHeader.setPadding(5);

        itemsTable.addCell(itemHeader);
        itemsTable.addCell(quantityHeader);
        itemsTable.addCell(rateHeader);
        itemsTable.addCell(amountHeader);

        // Add item rows
        for (Item item : invoice.getItems()) {
            PdfPCell nameCell = new PdfPCell(new Phrase(item.getName()));
            PdfPCell qtyCell = new PdfPCell(new Phrase(item.getQuantity()));
            PdfPCell rateCell = new PdfPCell(new Phrase(String.format("%.2f", item.getRate())));
            PdfPCell amountCell = new PdfPCell(new Phrase(String.format("%.2f", item.getAmount())));

            nameCell.setPadding(5);
            qtyCell.setPadding(5);
            rateCell.setPadding(5);
            amountCell.setPadding(5);

            itemsTable.addCell(nameCell);
            itemsTable.addCell(qtyCell);
            itemsTable.addCell(rateCell);
            itemsTable.addCell(amountCell);
        }

        document.add(itemsTable);
        document.close();

        log.info("PDF generated successfully at {}", filePath);
    }
}