package com.huce.project.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huce.project.entity.PaymentEntity;
import com.huce.project.entity.PaymentStatus;

import io.lettuce.core.dynamic.annotation.Param;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    Optional<PaymentEntity> findByTransactionId(String transactionId);

    List<PaymentEntity> findByPaymentStatusAndPaymentDateBefore(PaymentStatus status, LocalDateTime dateTime);

    @Query(value = "SELECT * FROM project_esim.payments WHERE order_id = :orderId", nativeQuery = true)
    Optional<List<PaymentEntity>> findByOrderId(@Param("orderId") int orderId);
}
