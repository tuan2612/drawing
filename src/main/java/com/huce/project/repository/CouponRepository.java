package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huce.project.entity.CouponEntity;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {
    boolean existsByCouponCode(String couponCode);
}
