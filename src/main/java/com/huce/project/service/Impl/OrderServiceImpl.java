package com.huce.project.service.Impl;

import com.huce.project.entity.ComposeKeyEsimOption;
import com.huce.project.entity.EsimOption;
import com.huce.project.entity.OrderEntity;
import com.huce.project.entity.OrderItemEntity;
import com.huce.project.repository.EsimOptionRepository;
import com.huce.project.repository.OrderItemRepository;
import com.huce.project.repository.OrderRepository;
import com.huce.project.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EsimOptionRepository esimOptionRepository;

    @Override
    @Transactional
    public OrderEntity addOrder(OrderEntity form) {
        try {
            // Tạo đơn hàng
            OrderEntity orderEntity = OrderEntity.builder()
                    .email(form.getEmail())
                    .fullName(form.getFullName())
                    .phoneNumber(form.getPhoneNumber())
                    .totalAmount(form.getTotalAmount())
                    .createdAt(LocalDateTime.now())
                    .build();

            // Lưu đơn hàng để lấy ID
            orderRepository.save(orderEntity);

            // Tạo các mục đơn hàng
            if (form.getOrderItems() != null && !form.getOrderItems().isEmpty()) {
                List<OrderItemEntity> orderItems = form.getOrderItems().stream()
                        .map(itemDTO -> {
                            // Tạo khóa tổng hợp
                            ComposeKeyEsimOption compositeKey = new ComposeKeyEsimOption();
                            compositeKey.setEsim_id(itemDTO.getEsimOption().getEsim().getEsimId());
                            compositeKey.setOption_id(itemDTO.getEsimOption().getOption().getOptionId());

                            // Tìm EsimOption
                            Optional<EsimOption> esimOptionOpt = esimOptionRepository.findById(compositeKey);
                            if (esimOptionOpt.isEmpty()) {
                                throw new EntityNotFoundException("EsimOption not found");
                            }

                            EsimOption esimOption = esimOptionOpt.get();

                            // Kiểm tra và cập nhật số lượng
                            long currentQuantity = esimOption.getOption_amount();
                            if (currentQuantity < itemDTO.getQuantity()) {
                                throw new RuntimeException("quantity of stock");
                            }
                            esimOption.setOption_amount(currentQuantity - itemDTO.getQuantity());
                            esimOption.setOption_sale(itemDTO.getQuantity() + esimOption.getOption_sale());
                            esimOptionRepository.save(esimOption);

                            // Tạo OrderItemEntity
                            return OrderItemEntity.builder()
                                    .order(orderEntity)
                                    .quantity(itemDTO.getQuantity())
                                    .price(itemDTO.getPrice())
                                    .esimOption(esimOption)
                                    .build();
                        })
                        .collect(Collectors.toList());

                orderItemRepository.saveAll(orderItems);
                orderEntity.setOrderItems(orderItems);
            }

            return orderEntity;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteOrder(int orderId) {
        try {
            Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isEmpty()) {
                throw new EntityNotFoundException("Not found orderEntity");
            }

            OrderEntity order = orderOpt.get();

            // Khôi phục số lượng trong kho
            if (order.getOrderItems() != null) {
                for (OrderItemEntity item : order.getOrderItems()) {
                    EsimOption esimOption = item.getEsimOption();
                    long currentQuantity = esimOption.getOption_amount();
                    esimOption.setOption_amount(currentQuantity + item.getQuantity());
                    esimOption.setOption_sale(esimOption.getOption_sale() - item.getQuantity());
                    esimOptionRepository.save(esimOption);
                }
            }

            // Xóa đơn hàng
            orderRepository.delete(order);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa đơn hàng: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void deleteExpiredOrders() {
        try {
            LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
            List<OrderEntity> expiredOrders = orderRepository.findOrdersOlderThan(expirationTime);

            for (OrderEntity order : expiredOrders) {
                try {
                    // Khôi phục số lượng trong kho
                    if (order.getOrderItems() != null) {
                        for (OrderItemEntity item : order.getOrderItems()) {
                            EsimOption esimOption = item.getEsimOption();
                            long currentQuantity = esimOption.getOption_amount();
                            esimOption.setOption_amount(currentQuantity + item.getQuantity());
                            esimOption.setOption_sale(esimOption.getOption_sale() - item.getQuantity());
                            esimOptionRepository.save(esimOption);
                        }
                    }

                    // Xóa đơn hàng
                    orderRepository.delete(order);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public OrderEntity getOrderById(int orderId) {
        return orderRepository.findById(orderId).orElse(null);

    }
}