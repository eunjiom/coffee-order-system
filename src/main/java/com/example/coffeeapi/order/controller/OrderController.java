package com.example.coffeeapi.order.controller;

import com.example.coffeeapi.order.dto.OrderRequest;
import com.example.coffeeapi.order.dto.OrderResponse;
import com.example.coffeeapi.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coffee/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문/결제 API
    @PostMapping
    public OrderResponse order(
            @RequestBody @Valid OrderRequest request
    ) {
        return orderService.order(request);
    }
}