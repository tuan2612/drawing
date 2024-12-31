package com.huce.project.specification;

import org.springframework.data.jpa.domain.Specification;

import com.huce.project.entity.CountryEntity;
import com.huce.project.form.SearchForm;
import com.huce.project.validation.SpecificationImpl;

public class CountrySpecification {
    public static final String SEARCH = "search";
    public static Specification<CountryEntity> buildWhere(SearchForm form){
        if (form==null) {
            return null;   
        }
        Specification<CountryEntity> WhereCountry= new SpecificationImpl(SEARCH,form.getSearch());
        return Specification.where(WhereCountry);

    }

}
