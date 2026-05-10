package com.example.coffeeapi.order.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

// 주문 요청 DTO
public record OrderRequest(

        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        @NotNull(message = "메뉴 ID는 필수입니다.")
        List<Long> menuIds
) {
}