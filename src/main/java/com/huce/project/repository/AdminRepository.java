package com.huce.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huce.project.entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    Optional<AdminEntity> findByUsername(String userName);
    boolean existsByUsername(String username);
}