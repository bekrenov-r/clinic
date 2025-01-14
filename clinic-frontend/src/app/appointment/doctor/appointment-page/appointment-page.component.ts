import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AppointmentService} from "../../appointment.service";
import {Observable} from "rxjs";
import {Appointment} from "../../../models/appointment";
import {ActivatedRoute, Router} from "@angular/router";
import {SuccessModalComponent} from "../../../common/components/success-modal/success-modal.component";

@Component({
  selector: 'app-appointment-page',
  templateUrl: './appointment-page.component.html',
  styleUrls: ['./appointment-page.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppointmentPageComponent implements OnInit {
  @ViewChild(SuccessModalComponent) successModal: SuccessModalComponent;

  appointment: Appointment;

  constructor(
    private appointmentService: AppointmentService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.fetchAppointment().subscribe(res => this.appointment = res);
  }

  fetchAppointment(): Observable<Appointment> {
    const id: number = +this.route.snapshot.paramMap.get('id');
    return this.appointmentService.getAppointmentById(id);
  }

  save() {
    this.saveAppointment().subscribe({
      next: res => {
        this.appointment = res;
        this.successModal.show('Appointment updated.');
      }
    });
  }

  saveAndFinish() {
    this.saveAppointment().subscribe({
      next: () => this.finishAppointment().subscribe(
        () => this.router.navigate(['/doctor/appointments'])
      )
    });
  }

  private saveAppointment(): Observable<Appointment> {
    const body = {
      details: this.appointment.details,
      prescription: this.appointment.prescription,
    };
    return this.appointmentService.updateAppointment(this.appointment.id, body);
  }

  private finishAppointment(): Observable<void> {
    return this.appointmentService.finishAppointment(this.appointment.id);
  }
}
