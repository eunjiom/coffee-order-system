package com.example.coffeeapi.point.controller;

import com.example.coffeeapi.point.dto.PointChargeRequest;
import com.example.coffeeapi.point.dto.PointResponse;
import com.example.coffeeapi.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coffee/points")
public class PointController {

    private final PointService pointService;

    // 포인트 충전 API
    @PostMapping("/charge")
    public PointResponse charge(
            @RequestBody @Valid PointChargeRequest request
    ) {
        return pointService.charge(request);
    }
}