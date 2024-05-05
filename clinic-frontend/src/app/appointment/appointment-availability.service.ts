import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AppointmentAvailabilityService {

  constructor(private http: HttpClient) { }

  getAvailableTimesByDepartment(departmentId: number, date: string): Observable<string[]> {
    return this.http.get<string[]>(`${environment.apiBaseUrl}/appointments/availability/department`, {
      params: {
        departmentId: departmentId,
        date: date
      }
    })
  }

  getAvailableTimesByDoctor(doctorId: number, date: string): Observable<string[]> {
    return this.http.get<string[]>(`${environment.apiBaseUrl}/appointments/availability/doctor`, {
      params: {
        doctorId: doctorId,
        date: date
      }
    })
  }

  getAllHolidays(): Observable<Date[]> {
    return this.http.get<Date[]>(`${environment.apiBaseUrl}/holidays`);
  }
}
