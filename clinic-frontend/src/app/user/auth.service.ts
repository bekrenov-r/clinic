import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  authenticate(email: string, password: string): Observable<string> {
    const requestParams = new HttpParams().set('username', email).set('password', password);
    return this.http.get(`${environment.apiBaseUrl}/authenticate`, { responseType: 'text', params: requestParams });
  }
}
