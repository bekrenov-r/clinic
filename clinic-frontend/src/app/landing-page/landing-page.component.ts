import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../user/login/auth.service";
import {Router} from "@angular/router";
import * as bootstrap from "bootstrap";
import Modal from "bootstrap/js/dist/modal";

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {
  @ViewChild('scheduleAppointmentModal') scheduleAppointmentModal: ElementRef;

  private bsScheduleAppointmentModal: bootstrap.Modal;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if(this.authService.hasAuthentication()) {
      if(this.authService.userHasRole('DOCTOR')) {
        this.router.navigate(['/doctor/home']);
      } else if(this.authService.userHasRole('PATIENT')) {
        this.router.navigate(['/patient/home']);
      }
    }
  }

  showScheduleAppointmentModal(): void {
    const modal: HTMLDivElement = this.scheduleAppointmentModal.nativeElement;
    this.bsScheduleAppointmentModal = Modal.getOrCreateInstance(modal);
    this.bsScheduleAppointmentModal.show();
  }
}
