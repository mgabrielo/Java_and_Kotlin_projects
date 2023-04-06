import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Employ } from '../employ';
import { EmployService } from '../employ.service';

@Component({
  selector: 'app-update-employee',
  templateUrl: './update-employee.component.html',
  styleUrls: ['./update-employee.component.css']
})
export class UpdateEmployeeComponent implements OnInit{
 
    id:number = 0;
  employee: Employ= new Employ();

  constructor(private employeeService: EmployService,private router: Router,
    private route: ActivatedRoute){}

  ngOnInit(): void {
    this.id= this.route.snapshot.params['id'];
    this.employeeService.getEmployeeById(this.id).subscribe(data=>{
        this.employee =data;
      },error => console.log(error)
    );
  }

 
  onSubmission(){
      this.employeeService.updateEmploy(this.id, this.employee).subscribe(
        data=>{
          this.goToList()
        }, 
        error => console.log(error)
      );
  }


  goToList(){
    this.router.navigate(['/employees']);
  }
}
