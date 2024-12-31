package com.huce.project.controller;

import com.huce.project.dto.PaymentOrderDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.PaymentEntity;
import com.huce.project.form.PayPalForm;
import com.huce.project.form.PaymentForm;
import com.huce.project.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PayPalController {

    private final PayPalService paypalService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<ResponseAPIDTO<Map<String, String>>> createPayment(
            @RequestBody PayPalForm form) {

        try {
            Payment payment = paypalService.createPayment(
                    form.getTotal(),
                    form.getCurrency(),
                    form.getOrder_id(),
                    "paypal",
                    "sale",
                    "Payment description");

            Map<String, String> response = new HashMap<>();

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    response.put("redirect_url", link.getHref());
                }
            }
            response.put("payment_id", payment.getId());

            return ResponseEntity.ok(
                    ResponseAPIDTO.<Map<String, String>>builder()
                            .code(200)
                            .message("Payment created successfully")
                            .result(response)
                            .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseAPIDTO.<Map<String, String>>builder()
                            .code(400)
                            .message("Error creating payment: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/success")
    public ResponseEntity<ResponseAPIDTO<String>> successPayment(
            @RequestBody PaymentForm form) {
        try {
            Payment payment = paypalService.executePayment(form.getPaymentId(), form.getPayerId());

            return ResponseEntity.ok(
                    ResponseAPIDTO.<String>builder()
                            .code(200)
                            .message("Payment processed successfully")
                            .result(payment.getState())
                            .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseAPIDTO.<String>builder()
                            .code(400)
                            .message("Error processing payment: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<ResponseAPIDTO<String>> cancelPayment() {
        return ResponseEntity.ok(
                ResponseAPIDTO.<String>builder()
                        .code(200)
                        .message("Payment was cancelled")
                        .result("CANCELLED")
                        .build());
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<ResponseAPIDTO<String>> getPaymentStatus(
            @PathVariable String paymentId) {

        try {
            Payment payment = paypalService.getPayment(paymentId);

            return ResponseEntity.ok(
                    ResponseAPIDTO.<String>builder()
                            .code(200)
                            .message("Payment status retrieved successfully")
                            .result(payment.getState())
                            .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseAPIDTO.<String>builder()
                            .code(400)
                            .message("Error getting payment status: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseAPIDTO<List<PaymentOrderDTO>>> getPaymentByOrder(
            @PathVariable int orderId) {

        try {
            // Fetch the payments for the given order ID
            List<PaymentEntity> paymentEntities = paypalService.getPaymentsByOrderId(orderId)
                    .orElseThrow(() -> new NoSuchElementException("No payments found for order ID: " + orderId));

            // Map the list of PaymentEntity to a list of PaymentOrderDTO
            List<PaymentOrderDTO> paymentOrderDTOs = paymentEntities.stream()
                    .map(payment -> modelMapper.map(payment, PaymentOrderDTO.class))
                    .collect(Collectors.toList());

            // Build the success response
            return ResponseEntity.ok(
                    ResponseAPIDTO.<List<PaymentOrderDTO>>builder()
                            .code(200)
                            .message("Payments retrieved successfully")
                            .result(paymentOrderDTOs)
                            .build());
        } catch (NoSuchElementException e) {
            // Handle specific case where no payments are found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseAPIDTO.<List<PaymentOrderDTO>>builder()
                            .code(404)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            // Handle generic errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseAPIDTO.<List<PaymentOrderDTO>>builder()
                            .code(500)
                            .message("Error getting payment order: " + e.getMessage())
                            .build());
        }
    }

}