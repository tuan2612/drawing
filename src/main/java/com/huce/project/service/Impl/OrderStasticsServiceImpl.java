package com.huce.project.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huce.project.form.StatisticOrderForm;
import com.huce.project.repository.OrderStatisticsRepository;
import com.huce.project.service.OrderStatisticsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderStasticsServiceImpl implements OrderStatisticsService {

    private final OrderStatisticsRepository orderStatisticsRepository;
    @Override
    public StatisticOrderForm getSalesStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Map<String, Object>> result = orderStatisticsRepository.getSalesStatistics(startDate, endDate);
        
        if (result.isEmpty()) {
            return null; 
        }

        Map<String, Object> row = result.get(0);
    
        // Ánh xạ từ Map sang StatisticOrderForm
        return StatisticOrderForm.builder()
            .totalOrders(getLongValue(row.get("total_orders"))) 
            .totalRevenue(getBigDecimalValue(row.get("total_revenue"))) 
            .uniqueEsimsSold(getLongValue(row.get("unique_esims_sold"))) 
            .totalEsimsSold(getLongValue(row.get("total_esims_sold"))) 
            .completedRevenue(getBigDecimalValue(row.get("completed_revenue")))
            .pendingRevenue(getBigDecimalValue(row.get("pending_revenue")))
            .failedRevenue(getBigDecimalValue(row.get("failed_revenue")))
            .build();
    }
    
    // Hàm tiện ích để kiểm tra null và trả về giá trị mặc định hoặc giá trị chuyển đổi
    private Long getLongValue(Object value) {
        return value != null ? ((Number) value).longValue() : 0L; 
    }
    
    private BigDecimal getBigDecimalValue(Object value) {
        return value != null ? (BigDecimal) value : BigDecimal.ZERO; 
    }
    
}
