package com.example.coffeeapi.order.dto;

// 주문 응답 DTO
public record OrderResponse(

        Long orderId,
        int totalPrice

) {
}
