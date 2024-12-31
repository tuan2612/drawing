package com.huce.project.form;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

public class PayPalForm {
    private BigDecimal total;
    private String currency;
    private int order_id;
}
