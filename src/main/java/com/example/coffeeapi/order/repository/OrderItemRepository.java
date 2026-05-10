package com.example.coffeeapi.order.repository;

import com.example.coffeeapi.menu.dto.PopularMenuResponse;
import com.example.coffeeapi.order.domain.OrderItem;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

// 주문 상품 Repository
public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    // 최근 7일 인기 메뉴 TOP3 조회
    @Query("""
        SELECT new com.example.coffeeapi.menu.dto.PopularMenuResponse(
            m.id,
            m.name,
            COUNT(oi.id)
        )
        FROM OrderItem oi
        JOIN oi.menu m
        JOIN oi.order o
        WHERE o.createdAt >= :sevenDaysAgo
        GROUP BY m.id, m.name
        ORDER BY COUNT(oi.id) DESC
        """)
    List<PopularMenuResponse> findPopularMenus(
            LocalDateTime sevenDaysAgo,
            Pageable pageable
    );
}