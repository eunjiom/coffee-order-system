package com.example.coffeeapi.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JPA 기본 생성자
@Entity // JPA 엔티티 등록
@Table(name = "users") // 테이블명 지정
public class User {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    // 사용자 보유 포인트
    private int point;

    // 사용자 생성자
    public User(int point) {
        this.point = point;
    }

    // 포인트 충전
    public void chargePoint(int amount) {
        this.point += amount;
    }

    // 포인트 사용
    public void usePoint(int amount) {

        // 포인트 부족 예외
        if (this.point < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        this.point -= amount;
    }
}