import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {AuthService} from "../../user/login/auth.service";
import {DoctorService} from "../doctor.service";
import {Router} from "@angular/router";
import {DoctorProfile} from "../../models/doctor";
import {Address} from "../../models/address";

@Component({
  selector: 'app-profile',
  templateUrl: './doctor-profile.component.html',
  styleUrls: ['./doctor-profile.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DoctorProfileComponent implements OnInit {
  doctor: DoctorProfile;

  constructor(
    private authService: AuthService,
    private doctorService: DoctorService,
    private router: Router
  ) {}

  ngOnInit() {
    this.requireRole('DOCTOR');
    this.doctorService.getDoctorProfile()
      .subscribe(res => this.doctor = res);
  }

  private requireRole(role: string) {
    if(!this.authService.userHasRole(role)) {
      this.router.navigate(['/login']);
    }
  }
}
