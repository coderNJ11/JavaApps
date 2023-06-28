package com.microservices.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.microservice.entity.Department;
import com.microservices.microservice.service.DepartmentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {
    

    @Autowired
    private DepartmentService service;


    @PostMapping("/")
    public Department saveDepartment(@RequestBody Department department){
        log.info("Department saveed ::: "+department.toString());
        return service.saveDepartment(department);
    }


    @GetMapping("/{id}")
    public Department findDepartmentById(@PathVariable("id") Long departmentId){
        log.info("Getting department by Id ::::: "+ departmentId);
        return service.findDepartmentById(departmentId);

    }


}
