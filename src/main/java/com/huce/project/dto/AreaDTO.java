package com.huce.project.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AreaDTO {
    @JsonProperty("area_id")
    private int areaId;
    @JsonProperty("area_name")
    private String areaName;

    private List<CountryEntityDTO> countries;

    @Data
    public static class CountryEntityDTO {
        @JsonProperty("country_id")
        private int countryId;
        @JsonProperty("country_name")
        private String countryName;
        @JsonProperty("country_code")
        private String countryCode;
        private String country_image;
    }
}
