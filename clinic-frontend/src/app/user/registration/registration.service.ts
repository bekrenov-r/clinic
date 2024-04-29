import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PatientRegistration } from 'src/app/models/patient-registration';
import { environment } from 'src/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) { }

  registerPatient(registration: PatientRegistration): Observable<any> {
    return this.http.post(environment.apiBaseUrl + '/register/patient', registration);
  }
}
