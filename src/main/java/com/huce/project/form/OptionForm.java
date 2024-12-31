package com.huce.project.form;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OptionForm {
    private int duration;

    private int data;

    private String unit;

    @JsonProperty("is_daily_plan")
    private Boolean isDailyPlan;

    private String description;

    private BigDecimal price;

    private String currency;
}
