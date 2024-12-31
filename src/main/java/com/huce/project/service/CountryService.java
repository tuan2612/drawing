package com.huce.project.service;

import java.util.List;

import com.huce.project.entity.CountryEntity;
import com.huce.project.form.SearchForm;

public interface CountryService {

   public List<CountryEntity> getAllCountry(SearchForm form ) ;
}