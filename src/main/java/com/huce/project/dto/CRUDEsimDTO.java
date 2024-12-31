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
public class CRUDEsimDTO {

    @JsonProperty("esim_name")
    private String esimName;

    @JsonProperty("esim_tag")
    private Boolean esimTag;
    @JsonProperty("country_id")
    private int countryEntityCountryId;
    
    private Boolean hotspot;

    private String policy;

    private String expiration;

    @JsonProperty("voice_calls_sms")
    private Boolean voiceCallsSms;

    private Boolean verified;

    private String description;

    private String provider;

    @JsonProperty("esim_image")
    private String esimImage;

}
