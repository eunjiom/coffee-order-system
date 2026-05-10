package com.example.coffeeapi.menu.controller;

import com.example.coffeeapi.menu.dto.MenuResponse;
import com.example.coffeeapi.menu.dto.PopularMenuResponse;
import com.example.coffeeapi.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coffee/menus")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuResponse> getMenus() {
        return menuService.getMenus();
    }

    // 인기 메뉴 조회 API
    @GetMapping("/popular")
    public List<PopularMenuResponse> getPopularMenus() {

        return menuService.getPopularMenus();
    }
}