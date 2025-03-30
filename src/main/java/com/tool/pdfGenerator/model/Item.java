package com.tool.pdfGenerator.model;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @NotBlank(message = "Item name is required")
    private String name;

    @NotBlank(message = "Quantity is required")
    private String quantity;

    @NotNull(message = "Rate is required")
    @Positive(message = "Rate must be positive")
    private Double rate;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}