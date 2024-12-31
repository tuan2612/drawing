package com.huce.project.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaymentForm {
    @JsonProperty("PayerID")
    private String payerId;
    private String paymentId;
    
}
