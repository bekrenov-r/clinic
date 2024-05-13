import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpParams, HttpRequest} from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  authenticate(email: string, password: string): Observable<string> {
    const requestParams = new HttpParams().set('username', email).set('password', password);
    return this.http.get(`${environment.apiBaseUrl}/authenticate`, { responseType: 'text', params: requestParams })
      .pipe(
        tap(token => localStorage.setItem(environment.authTokenStorageKey, token))
      );
  }

  isAuthenticated(): boolean {
    return localStorage.getItem(environment.authTokenStorageKey) !== null;
  }

  logout(): void {
    localStorage.removeItem(environment.authTokenStorageKey);
  }
}

@Injectable()
export class AuthorizationHeaderInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token: string = localStorage.getItem(environment.authTokenStorageKey);
    request = request.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next.handle(request);
  }
}
