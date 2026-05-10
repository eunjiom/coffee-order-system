package com.example.coffeeapi.order.service;

import com.example.coffeeapi.menu.domain.Menu;
import com.example.coffeeapi.menu.repository.MenuRepository;
import com.example.coffeeapi.order.domain.CoffeeOrder;
import com.example.coffeeapi.order.domain.OrderItem;
import com.example.coffeeapi.order.dto.OrderRequest;
import com.example.coffeeapi.order.dto.OrderResponse;
import com.example.coffeeapi.order.repository.RealTimeOrderSender;
import com.example.coffeeapi.order.repository.OrderRepository;
import com.example.coffeeapi.user.domain.User;
import com.example.coffeeapi.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final RealTimeOrderSender realTimeOrderSender;

    // 주문/결제 서비스
    @Transactional
    public OrderResponse order(OrderRequest request) {

        // 사용자 조회
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        // 메뉴 조회
        List<Menu> menus =
                menuRepository.findAllById(request.menuIds());

        // 메뉴 존재 여부 검증
        if (menus.size() != request.menuIds().size()) {
            throw new IllegalArgumentException(
                    "존재하지 않는 메뉴가 포함되어 있습니다."
            );
        }

        // 총 주문 금액 계산
        int totalPrice = menus.stream()
                .mapToInt(Menu::getPrice)
                .sum();

        // 포인트 차감
        user.usePoint(totalPrice);

        // 주문 생성
        CoffeeOrder order =
                new CoffeeOrder(user.getId(), totalPrice);

        // 주문 상품 추가
        for (Menu menu : menus) {

            OrderItem orderItem =
                    new OrderItem(
                            order,
                            menu,
                            menu.getPrice()
                    );

            order.addOrderItem(orderItem);
        }

        // 주문 저장
        CoffeeOrder savedOrder =
                orderRepository.save(order);

        // 실시간 주문 데이터 전송
        realTimeOrderSender.send(
                user.getId(),
                request.menuIds(),
                totalPrice
        );

        // 응답 반환
        return new OrderResponse(
                savedOrder.getId(),
                totalPrice
        );
    }
}