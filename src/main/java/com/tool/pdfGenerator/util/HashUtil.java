package com.tool.pdfGenerator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tool.pdfGenerator.model.Invoice;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HashUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateHash(Invoice invoice) {
        try {
            String invoiceJson = objectMapper.writeValueAsString(invoice);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(invoiceJson.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash for invoice", e);
        }
    }
}