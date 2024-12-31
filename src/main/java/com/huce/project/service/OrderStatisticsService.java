package com.huce.project.service;

import java.time.LocalDateTime;

import com.huce.project.form.StatisticOrderForm;

public interface OrderStatisticsService {
    StatisticOrderForm getSalesStatistics(LocalDateTime startDate, LocalDateTime endDate);
}
