package com.example.coffeeapi.order.repository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RealTimeOrderSender {

    // 실시간 주문 데이터 전송
    public void send(
            Long userId,
            List<Long> menuIds,
            int totalPrice
    ) {

        // Mock 외부 플랫폼 전송
        log.info(
                "실시간 주문 데이터 전송 완료 userId={}, menuIds={}, totalPrice={}",
                userId,
                menuIds,
                totalPrice
        );
    }
}