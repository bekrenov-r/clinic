import {AfterViewInit, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {PatientService} from "../patient.service";
import {PatientProfile} from "../../models/patient";

@Component({
  selector: 'app-patient-profile',
  templateUrl: './patient-profile.component.html',
  styleUrls: ['./patient-profile.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PatientProfileComponent implements OnInit {
  patient: PatientProfile;

  constructor(private patientService: PatientService) {}

  ngOnInit(): void {
    console.log('on init')
    this.patientService.getProfile().subscribe(profile => {
      console.log(profile)
      this.patient = profile
    })
  }
}
