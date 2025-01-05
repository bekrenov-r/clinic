import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {PatientProfile} from "../models/patient";

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  constructor(private http: HttpClient) { }

  getProfile(): Observable<PatientProfile> {
    console.log('getting profile')
    return this.http.get<PatientProfile>(environment.apiBaseUrl + '/patients');
  }
}
