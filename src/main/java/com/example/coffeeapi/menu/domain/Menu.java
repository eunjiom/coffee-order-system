package com.example.coffeeapi.menu.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "menus")
public class Menu {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    // 메뉴 이름
    private String name;

    // 메뉴 가격
    private int price;

    // 메뉴 생성용 생성자
    public Menu(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
