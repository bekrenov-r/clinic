import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Appointment} from "../models/appointment/appointment";
import {PatientAppointmentRequest} from "../models/appointment/patient-appointment-request";

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(private http: HttpClient) { }

  getAllAppointments(status?: string): Observable<Appointment[]> {
    let params = status ? {status} : undefined;
    return this.http.get<any>(`${environment.apiBaseUrl}/appointments`, {params: params})
      .pipe(
        map(res => res['content'])
      );
  }

  createAppointmentAsPatient(body: PatientAppointmentRequest): Observable<any> {
    return this.http.post(`${environment.apiBaseUrl}/appointments/patient`, body);
  }

  cancelAppointment(id: number): Observable<any> {
    return this.http.delete(`${environment.apiBaseUrl}/appointments/${id}/cancel`);
  }
}
