import { Component } from '@angular/core';
import {AuthService} from "../user/login/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent {

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if(this.authService.isAuthenticated()) {
      this.router.navigate(['/patient/home']);
    }
  }
}
