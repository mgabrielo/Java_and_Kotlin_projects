package com.example.springbootangular.employmanager.repository;

import com.example.springbootangular.employmanager.model.Employ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployRepository extends JpaRepository<Employ,Long> {
    Optional findEmployeeById(Long id);
}
