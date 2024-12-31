package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huce.project.entity.OptionEntity;

public interface OptionRepository extends JpaRepository<OptionEntity, Integer> {
}
