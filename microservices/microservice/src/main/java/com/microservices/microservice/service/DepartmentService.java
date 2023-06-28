package com.microservices.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.microservice.entity.Department;
import com.microservices.microservice.repository.DepartmentRepository;

@Service
public class DepartmentService {

    
    @Autowired
    private DepartmentRepository repository;
 
    public Department saveDepartment(Department department){
        return repository.save(department);    
    }

    public Department findDepartmentById(Long departmentId) {
        return repository.findByDepartmentId(departmentId);
    }


}
