package com.huce.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "countries")
public class CountryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private int countryId;

    @Column(name = "country_code", unique = true, nullable = false, length = 2)
    private String countryCode;

    @Column(name = "country_name", nullable = false)
    private String countryName;
    @Column(nullable = false)
    private String country_image;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "area_id")
    private AreaEntity area;
    @JsonManagedReference
    @OneToMany(mappedBy = "countryEntity")
    private List<EsimEntity> esimEntity;

}
