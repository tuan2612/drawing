package com.huce.project.dto;

import java.math.BigDecimal;
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
public class EsimDTO {
    @JsonProperty("esim_id")
    private Long esimId;
    @JsonProperty("esim_name")
    private String esimName;
    @JsonProperty("esim_tag")
    private Boolean esimTag;
    private Boolean hotspot;
    private String policy;
    @JsonProperty("voice_calls_sms")
    private Boolean voiceCallsSms;

    private Boolean verified;

    private String description;

    private String provider;
    @JsonProperty("esim_image")
    private String esimImage;
    @JsonProperty("esim_options")
    private List<EsimOptionDTO> esimOptions;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class EsimOptionDTO {
        @JsonProperty("option_id")
        private Long optionOptionId;
        @JsonProperty("duration")
        private Integer optionDuration;
        @JsonProperty("data")
        private Integer optionData;
        @JsonProperty("unit")
        private String optionUnit;
        @JsonProperty("is_daily_plan")
        private Boolean optionIsDailyPlan;
        @JsonProperty("price")
        private BigDecimal optionPrice;
        @JsonProperty("description")
        private String optionDescription;
        @JsonProperty("currency")
        private String optionCurrency;
        private Long option_amount;
    }
}
