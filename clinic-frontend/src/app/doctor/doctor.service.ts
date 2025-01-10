import { Injectable } from '@angular/core';
import {HttpClient, HttpContext} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {PersonDto} from "../models/person-dto";
import {DoctorProfile, DoctorPublicProfile} from "../models/doctor";
import {REQUIRES_AUTH} from "../user/login/auth.service";

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  constructor(private http: HttpClient) { }

  getDoctorsByDepartment(departmentId: number): Observable<PersonDto[]> {
    return this.http.get<PersonDto[]>(`${environment.apiBaseUrl}/doctors/department/${departmentId}`);
  }

  getDoctorProfile(): Observable<DoctorProfile> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<DoctorProfile>(`${environment.apiBaseUrl}/doctors/profile`, { context: context });
  }

  getDoctorPublicProfile(doctorId: number): Observable<DoctorPublicProfile> {
    return this.http.get<DoctorPublicProfile>(`${environment.apiBaseUrl}/doctors/${doctorId}/profile`);
  }
}
