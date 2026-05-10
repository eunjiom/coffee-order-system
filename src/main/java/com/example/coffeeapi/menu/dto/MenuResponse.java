package com.example.coffeeapi.menu.dto;

import com.example.coffeeapi.menu.domain.Menu;

public record MenuResponse(
        Long menuId,
        String name,
        int price
) {

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice()
        );
    }
}
