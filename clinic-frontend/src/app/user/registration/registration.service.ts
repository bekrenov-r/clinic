import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import {PatientRegistration} from "../../models/patient";
@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) { }

  registerPatient(registration: PatientRegistration): Observable<any> {
    return this.http.post(environment.apiBaseUrl + '/register/patient', registration);
  }

  activateAccount(activationToken: string): Observable<string> {
    return this.http.post(environment.apiBaseUrl + '/users/activate', null, {
      params: {token: activationToken},
      responseType: 'text'
    });
  }
}
