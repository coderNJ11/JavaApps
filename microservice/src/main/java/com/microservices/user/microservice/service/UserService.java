package com.microservices.user.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservices.user.microservice.VO.Department;
import com.microservices.user.microservice.VO.ResponseTemplateVo;
import com.microservices.user.microservice.entity.User;
import com.microservices.user.microservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
   
    @Autowired
    private UserRepository repository;


    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    public User saveUser(User user){
        log.info("Saving the user :::: "+user.toString());
        return repository.save(user);
    }


    public User findByUserId(Long userId) {
        log.info("finding the user by id :::: "+ userId);
        return repository.findByUserId(userId);
    }

    
    public ResponseTemplateVo getUserWithDepartment(Long userId){
        log.info("finding the user by id :::: "+ userId);
        ResponseTemplateVo responseTemplateVo = new ResponseTemplateVo();
        User user = repository.findByUserId(userId);
        responseTemplateVo.setUser(user);

        responseTemplateVo.setDepartment( getRestTemplate().getForObject("http://DEPARTMENT-SERVICE/departments/" + user.getDepartmentId(), Department.class ) );

        return responseTemplateVo;
    }
    
}
