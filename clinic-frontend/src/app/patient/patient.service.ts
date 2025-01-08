import { Injectable } from '@angular/core';
import {HttpClient, HttpContext} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {PatientProfile} from "../models/patient";
import {REQUIRES_AUTH} from "../user/login/auth.service";
import {PersonDto} from "../models/person-dto";

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  constructor(private http: HttpClient) { }

  getProfile(): Observable<PatientProfile> {
    const context: HttpContext = new HttpContext().set(REQUIRES_AUTH, true);
    return this.http.get<PatientProfile>(environment.apiBaseUrl + '/patients', {context});
  }

  getPatientByPesel(pesel: string): Observable<PersonDto> {
    return this.http.get<PersonDto>(`${environment.apiBaseUrl}/patients/pesel/${pesel}`)
  }
}
