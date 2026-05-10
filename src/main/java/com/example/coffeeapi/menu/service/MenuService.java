package com.example.coffeeapi.menu.service;

import com.example.coffeeapi.menu.dto.MenuResponse;
import com.example.coffeeapi.menu.repository.MenuRepository;
import com.example.coffeeapi.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.coffeeapi.menu.dto.PopularMenuResponse;

import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final OrderItemRepository orderItemRepository;

    public List<MenuResponse> getMenus() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .toList();
    }

    // 인기 메뉴 조회
    public List<PopularMenuResponse> getPopularMenus() {

        // 최근 7일 계산
        LocalDateTime sevenDaysAgo =
                LocalDateTime.now().minusDays(7);

        // TOP3 조회
        return orderItemRepository.findPopularMenus(
                sevenDaysAgo,
                PageRequest.of(0, 3)
        );
    }
}