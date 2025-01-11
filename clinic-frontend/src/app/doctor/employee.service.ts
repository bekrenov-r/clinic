import { Injectable } from '@angular/core';
import {HttpClient, HttpContext} from "@angular/common/http";
import {Employee, EmployeeRequest} from "../models/employee";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {REQUIRES_AUTH} from "../user/login/auth.service";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private http: HttpClient) { }

  getActiveEmployees(): Observable<Employee[]> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<Employee[]>(`${environment.apiBaseUrl}/employees`, {context: context});
  }

  getDismissedEmployees(): Observable<Employee[]> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<Employee[]>(`${environment.apiBaseUrl}/employees/dismissed`, {context: context});
  }

  createEmployee(body: EmployeeRequest): Observable<any> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.post(`${environment.apiBaseUrl}/employees`, body, {context: context});
  }
}
