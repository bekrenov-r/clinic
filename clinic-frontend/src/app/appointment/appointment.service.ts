import { Injectable } from '@angular/core';
import {HttpClient, HttpContext, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Appointment, DoctorAppointmentRequest, PatientAppointmentRequest} from "../models/appointment";
import {REQUIRES_AUTH} from "../user/login/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  constructor(private http: HttpClient) { }

  getAllAppointments(status?: string): Observable<Appointment[]> {
    let params = status ? {status} : undefined;
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<any>(`${environment.apiBaseUrl}/appointments`, {params: params, context: context});
  }

  getAppointmentById(id: number): Observable<Appointment> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<Appointment>(`${environment.apiBaseUrl}/appointments/${id}`, {context: context});
  }

  createAppointmentAsPatient(body: PatientAppointmentRequest, requiresAuth: boolean): Observable<any> {
    const context = new HttpContext().set(REQUIRES_AUTH, requiresAuth);
    return this.http.post(`${environment.apiBaseUrl}/appointments/patient`, body, {context: context});
  }

  createAppointmentAsDoctor(body: DoctorAppointmentRequest): Observable<any> {
    const context = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.post(`${environment.apiBaseUrl}/appointments/doctor`, body, {context: context});
  }

  updateAppointment(id: number, body: any): Observable<Appointment> {
    const context = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.put<Appointment>(`${environment.apiBaseUrl}/appointments/${id}`, body, {context: context});
  }

  confirmAppointment(appointmentId: number): Observable<any> {
    const context = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.patch(
      `${environment.apiBaseUrl}/appointments/${appointmentId}/confirm`,
      null,
      {context: context}
    );
  }

  finishAppointment(id: number): Observable<any> {
    const context = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.patch(
      `${environment.apiBaseUrl}/appointments/${id}/finish`,
      null,
      {context: context}
    );
  }

  cancelAppointment(id: number): Observable<any> {
    const context = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.delete(`${environment.apiBaseUrl}/appointments/${id}/cancel`, {context: context});
  }
}
