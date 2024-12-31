package com.huce.project.service;

import com.huce.project.entity.PaymentEntity;
import com.huce.project.entity.PaymentStatus;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PayPalService {

    Payment createPayment(
            BigDecimal total,
            String currency,
            int order_id,
            String method,
            String intent,
            String description) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    Payment getPayment(String paymentId) throws PayPalRESTException;

    PaymentEntity getPaymentByTransactionId(String transactionId);
    Optional<List<PaymentEntity>> getPaymentsByOrderId(int orderId);

    List<PaymentEntity> getAllPayments();

    PaymentEntity updatePaymentStatus(String transactionId, PaymentStatus status);
    
}
