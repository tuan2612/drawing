package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huce.project.entity.AboutUsEntity;

public interface AboutUsRepository extends JpaRepository<AboutUsEntity, Integer> {
    
}
