import { Component, OnInit } from '@angular/core';
import { Employ } from '../employ';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployService } from '../employ.service';

@Component({
  selector: 'app-employee-details',
  templateUrl: './employee-details.component.html',
  styleUrls: ['./employee-details.component.css']
})
export class EmployeeDetailsComponent implements OnInit {

  id: number=0;
  employee: Employ= new Employ();

  constructor(private route:  ActivatedRoute, private employeeService: EmployService){}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    
    this.employee = new Employ();
    this.employeeService.getEmployeeById(this.id).subscribe(
      data=>{
          this.employee=data;
      }
    );
  }

}
