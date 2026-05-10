package com.example.coffeeapi.user.repository;

import com.example.coffeeapi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}