package com.example.coffeeapi.point.service;

import com.example.coffeeapi.point.dto.PointChargeRequest;
import com.example.coffeeapi.point.dto.PointResponse;
import com.example.coffeeapi.user.domain.User;
import com.example.coffeeapi.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;

    // 포인트 충전 서비스 로직
    @Transactional
    public PointResponse charge(PointChargeRequest request) {

        // 사용자 조회
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 포인트 충전
        user.chargePoint(request.amount());

        // 응답 반환
        return new PointResponse(
                user.getId(),
                user.getPoint()
        );
    }
}