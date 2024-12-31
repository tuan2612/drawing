package com.huce.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayPalOrderDTO {
    private String currency;
    private String method;
    private String intent;
    private String description;
    private double amount;
}