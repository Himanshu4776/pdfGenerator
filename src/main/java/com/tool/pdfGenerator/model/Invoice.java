package com.tool.pdfGenerator.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @NotBlank(message = "Seller name is required")
    private String seller;

    @NotBlank(message = "Seller GSTIN is required")
    private String sellerGstin;

    @NotBlank(message = "Seller address is required")
    private String sellerAddress;

    @NotBlank(message = "Buyer name is required")
    private String buyer;

    @NotBlank(message = "Buyer GSTIN is required")
    private String buyerGstin;

    @NotBlank(message = "Buyer address is required")
    private String buyerAddress;

    @NotEmpty(message = "Items list cannot be empty")
    @Valid
    private List<Item> items;
}
