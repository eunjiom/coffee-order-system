package com.example.coffeeapi.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 포인트
    private int point;

    public User(int point) {
        this.point = point;
    }

    // 포인트 충전
    public void chargePoint(int amount) {
        this.point += amount;
    }
}