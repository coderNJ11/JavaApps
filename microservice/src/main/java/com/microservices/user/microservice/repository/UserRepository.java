package com.microservices.user.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.user.microservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    public User findByUserId(Long userId);
    
}
