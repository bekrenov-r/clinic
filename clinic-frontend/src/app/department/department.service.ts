import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Department} from "../models/department";
import {environment} from "../../environments/environment";

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
}
