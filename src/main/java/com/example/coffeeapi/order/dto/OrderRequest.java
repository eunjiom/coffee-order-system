package com.example.coffeeapi.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// 주문 요청 DTO
public record OrderRequest(

        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        @NotEmpty(message = "메뉴는 최소 1개 이상 선택해야 합니다.")
        List<Long> menuIds
) {
}