package com.example.coffeeapi.order.domain;

import com.example.coffeeapi.menu.domain.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JPA 기본 생성자
@Entity // JPA 엔티티 등록
@Table(name = "order_items") // 테이블명 지정
public class OrderItem {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private CoffeeOrder order;

    // 주문 메뉴 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    // 주문 당시 가격
    private int price;

    // 주문 상품 생성자
    public OrderItem(CoffeeOrder order, Menu menu, int price) {
        this.order = order;
        this.menu = menu;
        this.price = price;
    }
}