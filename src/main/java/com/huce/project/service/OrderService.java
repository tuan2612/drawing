package com.huce.project.service;

import com.huce.project.entity.OrderEntity;

public interface OrderService {
    OrderEntity addOrder(OrderEntity form);

    void deleteOrder(int orderId);
    OrderEntity getOrderById(int orderId);

}
