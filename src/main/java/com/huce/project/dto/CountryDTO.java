package com.huce.project.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CountryDTO {
    @JsonProperty("area_id")
    private int areaAreaId;
    @JsonProperty("country_id")
    private int countryId;
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("country_code")
    private String countryCode;
    private String country_image;
    @JsonProperty("esims")
    private Set<EsimEntityDTO> esimEntity;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class EsimEntityDTO {
        @JsonProperty("esim_id")
        private int esimId;
    }
}
