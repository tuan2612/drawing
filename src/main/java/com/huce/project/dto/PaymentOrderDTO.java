package com.huce.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huce.project.entity.OrderEntity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
public class PaymentOrderDTO {

    private int paymentId;

    @Column(name = "payment_amount", nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")

    private String paymentStatus;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "paypal_payer_id")
    private String paypalPayerId;

    @Column(name = "paypal_payer_email")
    private String paypalPayerEmail;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "payment_details", columnDefinition = "json")
    private String paymentDetails;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    private String expirationDate;
    @JsonProperty("order_id")
    private int orderEntityOrderId;

}
