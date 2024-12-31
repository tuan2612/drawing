package com.huce.project.service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.huce.project.entity.OrderEntity;
import com.huce.project.entity.OrderStatus;
import com.huce.project.entity.PaymentEntity;
import com.huce.project.entity.PaymentStatus;
import com.huce.project.repository.OrderRepository;
import com.huce.project.repository.PaymentRepository;
import com.huce.project.service.PayPalService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {

    private final APIContext apiContext;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private static final long PAYMENT_EXPIRATION_HOURS = 24;

    @Override
    public Payment createPayment(
            BigDecimal total,
            String currency,
            int orderId,
            String method,
            String intent,
            String description) throws PayPalRESTException {

        String baseUrl = "http://localhost:4200/payment/" + orderId;

        String cancelUrl = baseUrl + "/cancel";
        String successUrl = baseUrl + "/success";

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(total.setScale(2, RoundingMode.HALF_UP).toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Kiểm tra và tạo Payment
        Payment createdPayment = payment.create(apiContext);

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderEntity.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(orderEntity);
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .orderEntity(orderEntity)
                .paymentAmount(total)
                .currencyCode(currency)
                .paymentMethod("PAYPAL")
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(createdPayment.getId())
                .paymentDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusHours(PAYMENT_EXPIRATION_HOURS))
                .paymentDetails(createdPayment.toJSON())
                .build();

        paymentRepository.save(paymentEntity);

        log.info("Payment created successfully with ID: {}", createdPayment.getId());

        return createdPayment;
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        PaymentEntity paymentEntity = paymentRepository.findByTransactionId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Kiểm tra nếu thanh toán đã hết hạn
        if (LocalDateTime.now().isAfter(paymentEntity.getExpirationDate())) {
            paymentEntity.setPaymentStatus(PaymentStatus.FAILED);
            paymentEntity.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(paymentEntity);
            throw new com.huce.project.exeption.PaymentExpiredException(
                    "Payment link has expired. Please create a new payment.");
        }

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        paymentEntity.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentEntity.setPaypalPayerId(payerId);
        paymentEntity.setPaypalPayerEmail(executedPayment.getPayer().getPayerInfo().getEmail());
        paymentEntity.setPaymentDetails(executedPayment.toJSON());
        paymentEntity.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(paymentEntity);

        log.info("Payment with ID {} completed successfully", paymentId);

        return executedPayment;
    }

    // Scheduled task to check and update expired payments
    @Scheduled(fixedRate = 300000) // Runs every 5 minutes
    public void checkExpiredPayments() {
        LocalDateTime now = LocalDateTime.now();
        List<PaymentEntity> pendingPayments = paymentRepository.findByPaymentStatusAndPaymentDateBefore(
                PaymentStatus.PENDING,
                now);

        for (PaymentEntity payment : pendingPayments) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(now);
            log.info("Payment {} has expired and marked as FAILED", payment.getTransactionId());
            paymentRepository.save(payment);
        }
    }

    @Override
    public Payment getPayment(String paymentId) throws PayPalRESTException {
        PaymentEntity paymentEntity = paymentRepository.findByTransactionId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Kiểm tra nếu thanh toán đã hết hạn
        if (LocalDateTime.now().isAfter(paymentEntity.getExpirationDate())) {
            throw new com.huce.project.exeption.PaymentExpiredException(
                    "Payment link has expired. Please create a new payment.");
        }

        return Payment.get(apiContext, paymentId);
    }

    @Override
    public PaymentEntity getPaymentByTransactionId(String transactionId) {
        PaymentEntity payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Kiểm tra nếu thanh toán đã hết hạn
        if (LocalDateTime.now().isAfter(payment.getExpirationDate())) {
            throw new com.huce.project.exeption.PaymentExpiredException(
                    "Payment link has expired. Please create a new payment.");
        }

        return payment;
    }

    @Override
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity updatePaymentStatus(String transactionId, PaymentStatus status) {
        PaymentEntity payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Override
    public Optional<List<PaymentEntity>> getPaymentsByOrderId(int orderId) {
        return paymentRepository.findByOrderId(orderId);
        
    }
}
