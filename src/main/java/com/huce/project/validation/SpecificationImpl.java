package com.huce.project.validation;

import org.springframework.data.jpa.domain.Specification;

import com.huce.project.entity.CountryEntity;
import com.huce.project.specification.CountrySpecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SpecificationImpl implements Specification<CountryEntity> {
    private String key;
    private Object value;

    @Override
    public Predicate toPredicate(Root<CountryEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (value == null) {
            return null;
        }
        switch (key) {
            case CountrySpecification.SEARCH:

                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("countryName")),
                        "%" + value + "%");
            default:
                return null;
        }

    }
}
