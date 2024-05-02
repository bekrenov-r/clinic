import {Component, ElementRef, ViewChild, ViewEncapsulation} from '@angular/core';
import flatpickr from "flatpickr";

@Component({
  selector: 'app-patient-appointment-form',
  templateUrl: './patient-appointment-form.component.html',
  styleUrls: ['./patient-appointment-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PatientAppointmentFormComponent {
  @ViewChild('datePicker') datePicker: ElementRef;
  selectedDate: Date;

  ngOnInit(): void {
    flatpickr('#date', {
      defaultDate: new Date(),
      dateFormat: 'l, d M Y'
    });
  }
}
