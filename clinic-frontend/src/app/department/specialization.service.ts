import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SpecializationService {

  constructor(private http: HttpClient) { }

  getAllSpecializations(): Observable<any> {
    return this.http.get(`${environment.apiBaseUrl}/specializations`);
  }
}
