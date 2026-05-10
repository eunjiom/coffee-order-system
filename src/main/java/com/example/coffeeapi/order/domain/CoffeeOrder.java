package com.example.coffeeapi.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor // JPA 기본 생성자
@Entity // JPA 엔티티 등록
@Table(name = "orders") // 테이블명 지정
public class CoffeeOrder {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문한 사용자 ID
    private Long userId;

    // 총 주문 금액
    private int totalPrice;

    // 주문 생성 시간
    private LocalDateTime createdAt;

    // 주문 상품 목록
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 생성자
    public CoffeeOrder(Long userId, int totalPrice) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
    }

    // 주문 상품 추가
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }
}
