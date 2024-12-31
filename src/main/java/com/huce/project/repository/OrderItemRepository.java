package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huce.project.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity,Integer> {
    
}
