package com.huce.project.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.form.StatisticOrderForm;
import com.huce.project.service.OrderStatisticsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/statistics")
public class OrderStasticsController {
    private final OrderStatisticsService orderStatisticsService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseAPIDTO<StatisticOrderForm>> getSalesStatistics(
            @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end) {
        try {
            LocalDateTime startDate = StringUtils.hasText(start)
                    ? LocalDateTime.parse(start, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : LocalDateTime.now().minusMonths(1);

            LocalDateTime endDate = StringUtils.hasText(end)
                    ? LocalDateTime.parse(end, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : LocalDateTime.now();

            StatisticOrderForm statistics = orderStatisticsService.getSalesStatistics(startDate, endDate);

            return ResponseEntity.ok(
                    ResponseAPIDTO.<StatisticOrderForm>builder()
                            .code(200)
                            .message("Thống kê thành công")
                            .result(statistics)
                            .build());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(
                    ResponseAPIDTO.<StatisticOrderForm>builder()
                            .code(400)
                            .message("Định dạng ngày không hợp lệ. Sử dụng định dạng ISO_LOCAL_DATE_TIME")
                            .build());
        }
    }
}