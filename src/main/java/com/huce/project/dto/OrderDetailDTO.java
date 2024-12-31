package com.huce.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderDetailDTO {
    @JsonProperty("order_id")
    private int orderId;
    private String email;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    @JsonProperty("order_status")
    private String orderStatus;
    @JsonProperty("create_at")
    private String createdAt;
    private List<OrderItemDTO> orderItems;

    @Data
    public static class OrderItemDTO {
        @JsonProperty("order_item_id")
        private int orderItemId;
        private int quantity;

        private BigDecimal price;
        private EsimOptionDTO esimOption;

        @Data
        private static class EsimOptionDTO {
            @JsonProperty("esim_id")
            private int idEsim_id;
            @JsonProperty("option_id")
            private int idOption_id;
        }
    }
}
