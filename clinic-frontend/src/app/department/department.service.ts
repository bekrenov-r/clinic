import { Injectable } from '@angular/core';
import {HttpClient, HttpContext} from "@angular/common/http";
import {Observable} from "rxjs";
import {Department, DepartmentDetails, DepartmentRequest} from "../models/department";
import {environment} from "../../environments/environment";
import {REQUIRES_AUTH} from "../user/login/auth.service";

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  constructor(private http: HttpClient) { }

  getDepartmentsBySpecialization(specialization: string): Observable<Department[]> {
    return this.http.get<Department[]>(`${environment.apiBaseUrl}/departments`, {
      params: {spec: specialization}
    });
  }

  getDepartmentById(id: number): Observable<DepartmentDetails> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<DepartmentDetails>(
      `${environment.apiBaseUrl}/departments/${id}`,
      {context: context}
    );
  }

  updateDepartment(id: number, body: DepartmentRequest): Observable<DepartmentDetails> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.put<DepartmentDetails>(
      `${environment.apiBaseUrl}/departments/${id}`, body, {context: context}
    );
  }
}
