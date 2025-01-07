import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {AuthService} from "../../user/login/auth.service";
import {jwtDecode} from "jwt-decode";
import {environment} from "../../../environments/environment";
import {Router} from "@angular/router";

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor-home.component.html',
  styleUrls: ['./doctor-home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DoctorHomeComponent implements OnInit {
  firstName: string;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.requireRole('DOCTOR');
    this.setFirstName();
  }

  private requireRole(role: string) {
    if(!this.authService.userHasRole(role)) {
      this.router.navigate(['/']);
    }
  }

  private setFirstName() {
    const jwt: string = localStorage.getItem(environment.authTokenStorageKey);
    const jwtPayload: any = jwtDecode(jwt);
    this.firstName = jwtPayload['fname'];
  }

  isHeadOfDepartment(): boolean {
    return this.authService.userHasRole('HEAD_OF_DEPARTMENT');
  }
}
