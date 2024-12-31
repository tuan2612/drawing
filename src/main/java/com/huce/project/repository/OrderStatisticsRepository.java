package com.huce.project.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huce.project.entity.OrderEntity;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderStatisticsRepository extends JpaRepository<OrderEntity, Integer> {
    @Query(nativeQuery = true, value = """
        SELECT 
            COUNT(DISTINCT o.order_id) AS total_orders,
            SUM(o.total_amount) AS total_revenue,
            COUNT(DISTINCT oi.esim_id) AS unique_esims_sold,
            SUM(oi.quantity) AS total_esims_sold,
            SUM(CASE WHEN p.payment_status = 'COMPLETED' THEN p.payment_amount ELSE 0 END) AS completed_revenue,
            SUM(CASE WHEN p.payment_status = 'PENDING' THEN p.payment_amount ELSE 0 END) AS pending_revenue,
            SUM(CASE WHEN p.payment_status = 'FAILED' THEN p.payment_amount ELSE 0 END) AS failed_revenue
        FROM 
            orders o 
        LEFT JOIN order_items oi ON o.order_id = oi.order_id 
        LEFT JOIN payments p ON o.order_id = p.order_id 
        WHERE 
            o.created_at BETWEEN :startDate AND :endDate
    """)
    
    List<Map<String, Object>> getSalesStatistics(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
}
