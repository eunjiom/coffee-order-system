package com.example.coffeeapi.menu.dto;

// 인기 메뉴 응답 DTO
public record PopularMenuResponse(

        Long menuId,
        String name,
        Long orderCount
) {
}