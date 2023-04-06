package com.example.springbootangular.employmanager.service;

import com.example.springbootangular.employmanager.exception.ResourceNotFoundException;
import com.example.springbootangular.employmanager.model.Employ;
import com.example.springbootangular.employmanager.repository.EmployRepository;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class employee_service {
    private final EmployRepository employee_Repository;

    public employee_service(EmployRepository employee_Repo){
        this.employee_Repository = employee_Repo;
    }

    public Employ addEmployee(Employ employee){
        return employee_Repository.save(employee);
    }

    public List<Employ> findAllEmploys(){
        return  employee_Repository.findAll();
    }

    public Employ updateEmployee(Employ employee){
        return employee_Repository.save(employee);
    }

    public void deleteEmployee(Long id){
        employee_Repository.deleteById(id);
    }

    public List<Employ> findEmployee(Long id) throws Throwable {
        return (List<Employ>) employee_Repository.findEmployeeById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user id of: "+id+" not found"));
    }


}
