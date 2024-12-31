package com.huce.project.exeption;

public class PaymentExpiredException extends RuntimeException {
    public PaymentExpiredException(String message) {
        super(message);
    }
}