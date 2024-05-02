import {Component, ViewEncapsulation} from '@angular/core';
import {AuthService} from "../../user/login/auth.service";
import {environment} from "../../../environments/environment";
import {jwtDecode} from "jwt-decode";

@Component({
  selector: 'app-home',
  templateUrl: './patient-home.component.html',
  styleUrls: ['./patient-home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PatientHomeComponent {
  firstName: string;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const jwtPayload: any = jwtDecode(localStorage.getItem(environment.authTokenStorageKey));
    this.firstName = jwtPayload['fname'];
  }
}
