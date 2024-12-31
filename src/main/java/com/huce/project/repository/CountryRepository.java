package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huce.project.entity.CountryEntity;


public interface CountryRepository extends JpaRepository<CountryEntity,Integer >,JpaSpecificationExecutor<CountryEntity>{
    boolean existsByCountryId(int countryId);
}
