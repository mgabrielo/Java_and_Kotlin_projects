package com.example.springbootangular.employmanager;

import com.example.springbootangular.employmanager.exception.ResourceNotFoundException;
import com.example.springbootangular.employmanager.model.Employ;
import com.example.springbootangular.employmanager.repository.EmployRepository;
import com.example.springbootangular.employmanager.service.employee_service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api/v1/")
public class EmployController {
    private final employee_service employ_service;
    private final EmployRepository employRepository;

    public EmployController(employee_service employ_service,
                            EmployRepository employRepository){
        this.employ_service=employ_service;
        this.employRepository = employRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employ>> getAllEmployee(){
        List<Employ> employees =employ_service.findAllEmploys();
        return  new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping("/add")
    public  ResponseEntity<Employ> addEmployee(@RequestBody Employ employee){
        Employ new_employee= employ_service.addEmployee(employee);
        return new ResponseEntity<>(new_employee, HttpStatus.CREATED);
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<Employ> getEmployeeById(@PathVariable("id") Long id) {
       Employ employee = employRepository.findById(id)
               .orElseThrow(()-> new ResourceNotFoundException("user id of: "+id+" not found"));
       return ResponseEntity.ok(employee);
    }


    @PutMapping("/update/{id}")
    public  ResponseEntity<Employ> updateEmployee(@PathVariable("id") Long id, @RequestBody Employ employDetails){
        Employ employs = employRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user id of: "+id+" not found"));
        employs.setFirstname(employDetails.getFirstname());
        employs.setLastname(employDetails.getLastname());
        employs.setEmail(employDetails.getEmail());

        Employ updateEmploy= employRepository.save(employs);
        return ResponseEntity.ok(updateEmploy);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteEmployee(@PathVariable("id") Long id){
        Employ employed = employRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user id of: "+id+" not found"));
        employRepository.delete(employed);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
