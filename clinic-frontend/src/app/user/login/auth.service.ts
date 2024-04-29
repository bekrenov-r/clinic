import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly AUTH_TOKEN_STORAGE_KEY: string = 'clinic-auth-token'; 

  constructor(private http: HttpClient) { }

  authenticate(email: string, password: string): Observable<string> {
    const requestParams = new HttpParams().set('username', email).set('password', password);
    const response$: Observable<string> = this.http.get(`${environment.apiBaseUrl}/authenticate`, { responseType: 'text', params: requestParams });
    response$.subscribe(token => localStorage.setItem(this.AUTH_TOKEN_STORAGE_KEY, token));
    return response$;
  }

  isAuthenticated(): boolean {
    return localStorage.getItem(this.AUTH_TOKEN_STORAGE_KEY) !== null;
  }

  logout(): void {
    localStorage.removeItem(this.AUTH_TOKEN_STORAGE_KEY);
  }
}
