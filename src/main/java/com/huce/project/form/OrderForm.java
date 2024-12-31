package com.huce.project.form;
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
public class OrderForm {

    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("order_items")
    private List<OrderItemEntityDTO> orderItems;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class OrderItemEntityDTO {

        private int quantity;
        private BigDecimal price;
        private ComposeKeyDTO esimOption;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        public static class ComposeKeyDTO {
            @JsonProperty("esim_id")
            private int idEsim_id; 
            @JsonProperty("option_id")
            private int idOption_id; 
        }
    }
}
