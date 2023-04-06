import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employ } from '../employ';
import { EmployService } from '../employ.service';

@Component({
  selector: 'app-create-employee',
  templateUrl: './create-employee.component.html',
  styleUrls: ['./create-employee.component.css']
})
export class CreateEmployeeComponent implements OnInit{

  employee: Employ= new Employ();

  constructor(private employeeService: EmployService,
    private router: Router){}

  ngOnInit(): void {
    
  }

  saveEmployee(){
    this.employeeService.createEmployee(this.employee).subscribe(
      data=>{
            console.log(data)
            this.goToEmployeeList();
      },
      error=> console.log(error));
  }


  goToEmployeeList(){
      this.router.navigate(['/employees']);
  }

  onSubmit(){
    console.log(this.employee);
    this.saveEmployee();
  }

}
