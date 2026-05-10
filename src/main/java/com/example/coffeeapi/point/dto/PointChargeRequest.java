package com.example.coffeeapi.point.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// 포인트 충전 요청 DTO
public record PointChargeRequest(

        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        @Min(value = 1, message = "충전 금액은 1 이상이어야 합니다.")
        int amount
) {
}