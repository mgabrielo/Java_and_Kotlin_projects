import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employ } from '../employ';
import { EmployService } from '../employ.service';

@Component({
  selector: 'app-employ-list',
  templateUrl: './employ-list.component.html',
  styleUrls: ['./employ-list.component.css']
})
export class EmployListComponent implements OnInit {
  employs: Employ[] = [];
  constructor(private employeeService: EmployService,
    private router:Router){}
  ngOnInit(): void {
    this.getEmployees();
  }


  private getEmployees(){
    this.employeeService.getEmployeeList().subscribe(
      data=>{
        this.employs=data;
      }
    )
  }

  updateEmployee(id:number ){
    this.router.navigate(['update-employee',id]);
  }

  employeeDetails(id:number ){
    this.router.navigate(['employee-details',id]);
  }

  deleteEmployee(id:number ){
    this.employeeService.deleteEmploy(id).subscribe(
      data=>{
        console.log(data);
        this.getEmployees();
      }
    )
  }
}
