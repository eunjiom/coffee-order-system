package com.example.coffeeapi.order.repository;

import com.example.coffeeapi.order.domain.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

// 주문 Repository
public interface OrderRepository extends JpaRepository<CoffeeOrder, Long> {
}