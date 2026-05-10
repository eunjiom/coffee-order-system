package com.example.coffeeapi.order.repository;

import com.example.coffeeapi.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

// 주문 상품 Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}