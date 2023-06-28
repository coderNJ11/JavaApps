package com.microservices.apigateway.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @GetMapping("/userServiceFallBack")
    public String userServiceFallbackMethod(){
        return "User Service is taking Longer than expected";
    }
    
    @GetMapping("/departmentServiceFallBack")
    public String departmentServiceFallbackMethod(){
        return "Department Service is taking Longer than expected";
    }
    
}
