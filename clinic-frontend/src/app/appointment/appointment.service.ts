import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {PatientAppointmentRequest} from "../models/appointment";

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(private http: HttpClient) { }

  createAppointmentAsPatient(body: PatientAppointmentRequest): Observable<any> {
    return this.http.post(`${environment.apiBaseUrl}/appointments/patient`, body);
  }
}
