package com.huce.project.controller;

import com.huce.project.dto.OrderDTO;
import com.huce.project.dto.OrderDetailDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.OrderEntity;
import com.huce.project.form.OrderForm;
import com.huce.project.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @PostMapping("add")
    public ResponseEntity<ResponseAPIDTO<OrderDTO>> addOrder(@RequestBody OrderForm form) {
        try {

            OrderEntity orderEntity = modelMapper.map(form, OrderEntity.class);

            OrderEntity createdOrder = orderService.addOrder(orderEntity);

            OrderDTO responseForm = modelMapper.map(createdOrder, OrderDTO.class);

            // Build and return the success response
            return ResponseEntity.ok(
                    ResponseAPIDTO.<OrderDTO>builder()
                            .code(HttpStatus.OK.value())
                            .message("Order created successfully")
                            .result(responseForm)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();

            // Build and return the error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseAPIDTO.<OrderDTO>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while creating the order")
                            .result(null)
                            .build());
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseAPIDTO<Void>> deleteOrder(@PathVariable int orderId) {
        try {
            // Call service to delete order
            orderService.deleteOrder(orderId);

            return ResponseEntity.ok(
                    ResponseAPIDTO.<Void>builder()
                            .code(HttpStatus.OK.value())
                            .message("Order deleted successfully")
                            .result(null)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseAPIDTO.<Void>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while deleting the order")
                            .result(null)
                            .build());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseAPIDTO<OrderDetailDTO>> getDetailOrderById(@PathVariable int orderId) {
        OrderEntity order = orderService.getOrderById(orderId);

        if (order == null) { 
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseAPIDTO.<OrderDetailDTO>builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message("Order not found")
                            .build());
        }

        OrderDetailDTO orderDetailDTO = modelMapper.map(order, OrderDetailDTO.class);

        return ResponseEntity.ok(ResponseAPIDTO.<OrderDetailDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Order detail")
                .result(orderDetailDTO)
                .build());
    }

}
