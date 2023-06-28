package com.microservices.user.microservice.VO;

import com.microservices.user.microservice.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateVo {

    private User user;

    private Department department;
    
}
