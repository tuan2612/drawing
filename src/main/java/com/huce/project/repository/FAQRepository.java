package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huce.project.entity.FAQEntity;

public interface FAQRepository extends JpaRepository<FAQEntity,Integer> {
    
}
