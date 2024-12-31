package com.huce.project.form;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StatisticOrderForm {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long uniqueEsimsSold;
    private Long totalEsimsSold;
    private BigDecimal completedRevenue;
    private BigDecimal pendingRevenue;
    private BigDecimal failedRevenue;
}
