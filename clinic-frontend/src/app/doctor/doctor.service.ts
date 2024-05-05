import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {PersonDto} from "../models/person-dto";

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  constructor(private http: HttpClient) { }

  getDoctorsByDepartment(departmentId: number): Observable<PersonDto[]> {
    return this.http.get<PersonDto[]>(`${environment.apiBaseUrl}/doctors/department/${departmentId}`);
  }
}
