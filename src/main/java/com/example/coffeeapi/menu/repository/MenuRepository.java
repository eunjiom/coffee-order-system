package com.example.coffeeapi.menu.repository;

import com.example.coffeeapi.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
