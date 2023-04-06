import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employ } from './employ';

@Injectable({
  providedIn: 'root'
})
export class EmployService {

  private baseURL ="http://localhost:8080/api/v1"
  constructor(private httpclient : HttpClient) { }

  getEmployeeList(): Observable<Employ[]>{
    return this.httpclient.get<Employ[]>(`${this.baseURL}/all`);
  }

  createEmployee(employee: Employ): Observable<Object>{
    return this.httpclient.post(`${this.baseURL}/add`, employee);
  }

  getEmployeeById(id: number): Observable<Employ>{
    return this.httpclient.get<Employ>(`${this.baseURL}/find/${id}`);
  }

  updateEmploy(id: number,employee: Employ): Observable<Employ>{
    return this.httpclient.put<Employ>(`${this.baseURL}/update/${id}`, employee);
  }

  deleteEmploy(id: number): Observable<Object>{
    return this.httpclient.delete(`${this.baseURL}/delete/${id}`);
  }

}
