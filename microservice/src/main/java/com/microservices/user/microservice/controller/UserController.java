package com.microservices.user.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.user.microservice.VO.ResponseTemplateVo;
import com.microservices.user.microservice.entity.User;
import com.microservices.user.microservice.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users/")
@Slf4j
public class UserController {


    @Autowired
    private UserService service;



    @PostMapping("/")
    public User saveUser(@RequestBody User user){
        return service.saveUser(user) ;
    }
    

    @GetMapping("{id}")
    public ResponseTemplateVo getUserWithDepartment(@PathVariable("id") Long userId){
        log.info("getting value of User ");
        return service.getUserWithDepartment(userId);
    }
}
