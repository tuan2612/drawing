package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huce.project.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findByOrderId(int orderId);

    @Query("SELECT o FROM OrderEntity o WHERE o.createdAt <= :expiryTime and o.orderStatus = 'PENDING'")
    List<OrderEntity> findOrdersOlderThan(@Param("expiryTime") LocalDateTime expiryTime);
}
