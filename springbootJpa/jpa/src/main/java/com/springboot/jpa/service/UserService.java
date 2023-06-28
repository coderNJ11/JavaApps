package com.springboot.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sprinboot.jpa.dao.UserRepository;
import com.springboot.jpa.model.User;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;



    public List<User> getUsers(){
            return userRepository.findAll();   
    }

    
}
