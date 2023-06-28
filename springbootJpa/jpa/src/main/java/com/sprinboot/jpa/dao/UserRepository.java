package com.sprinboot.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jpa.model.User;

public interface UserRepository extends JpaRepository<User , Integer> {
    
}
