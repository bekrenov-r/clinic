import { Component } from '@angular/core';
import { AuthService } from '../user/login/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private authService: AuthService, private router: Router) {}

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  displaySignInButton(): boolean {
    return this.router.url !== '/login';
  }
  
  displaySignUpButton(): boolean {
    return this.router.url !== '/registration';
  }
}
