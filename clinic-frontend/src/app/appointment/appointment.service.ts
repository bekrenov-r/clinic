import { Injectable } from '@angular/core';
import {HttpClient, HttpContext, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Appointment} from "../models/appointment/appointment";
import {PatientAppointmentRequest} from "../models/appointment/patient-appointment-request";
import {REQUIRES_AUTH} from "../user/login/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  constructor(private http: HttpClient) { }

  getAllAppointments(status?: string): Observable<Appointment[]> {
    let params = status ? {status} : undefined;
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<any>(`${environment.apiBaseUrl}/appointments`, {params: params, context: context})
      .pipe(
        map(res => res['content'])
      );
  }

  createAppointmentAsPatient(body: PatientAppointmentRequest, requiresAuth: boolean): Observable<any> {
    const context = new HttpContext().set(REQUIRES_AUTH, requiresAuth);
    return this.http.post(`${environment.apiBaseUrl}/appointments/patient`, body, {context: context});
  }

  cancelAppointment(id: number): Observable<any> {
    const context = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.delete(`${environment.apiBaseUrl}/appointments/${id}/cancel`, {context: context});
  }
}
