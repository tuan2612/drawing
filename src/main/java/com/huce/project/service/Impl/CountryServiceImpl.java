package com.huce.project.service.Impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.huce.project.entity.CountryEntity;
import com.huce.project.form.SearchForm;
import com.huce.project.repository.CountryRepository;
import com.huce.project.service.CountryService;
import com.huce.project.specification.CountrySpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

   
    @Override
    public List<CountryEntity> getAllCountry(SearchForm form) {
        Specification<CountryEntity> where = CountrySpecification.buildWhere(form);
        return countryRepository.findAll(where);
    }
}
