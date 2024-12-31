package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huce.project.entity.BlogEntity;

public interface BlogRepository extends JpaRepository<BlogEntity,Integer>{
    
}
