import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { LoginFormComponent } from './user/login/login-form/login-form.component';
import { RegistrationFormComponent } from './user/registration/registration-form/registration-form.component';
import { RegistrationConfirmedComponent } from './user/registration/registration-confirmed/registration-confirmed.component';
import {PatientHomeComponent} from "./home/patient/patient-home.component";
import {
  PatientAppointmentFormComponent
} from "./appointment/patient/appointment-form/patient-appointment-form.component";
import {
  PatientAppointmentListComponent
} from "./appointment/patient/appointment-list/patient-appointment-list.component";
import {AboutComponent} from "./landing-page/about/about.component";
import {PatientProfileComponent} from "./patient/patient-profile/patient-profile.component";
import {UnregisteredAppointmentFormComponent} from "./appointment/unregistered/unregistered-appointment-form.component";
import {DoctorHomeComponent} from "./home/doctor/doctor-home.component";
import {DoctorAppointmentFormComponent} from "./appointment/doctor/appointment-form/doctor-appointment-form.component";
import {DoctorAppointmentListComponent} from "./appointment/doctor/appointment-list/doctor-appointment-list.component";
import {DoctorProfileComponent} from "./doctor/profile/doctor-profile.component";

const routes: Routes = [
  {path: '', component: LandingPageComponent},
  {path: 'about', component: AboutComponent},
  {path: 'login', component: LoginFormComponent},
  {path: 'registration', component: RegistrationFormComponent},
  {path: 'registration/confirmed', component: RegistrationConfirmedComponent},
  {path: 'patient/home', component: PatientHomeComponent},
  {path: 'patient/profile', component: PatientProfileComponent},
  {path: 'patient/create-appointment', component: PatientAppointmentFormComponent},
  {path: 'patient/appointments', component: PatientAppointmentListComponent},
  {path: 'doctor/home', component: DoctorHomeComponent},
  {path: 'doctor/profile', component: DoctorProfileComponent},
  {path: 'doctor/create-appointment', component: DoctorAppointmentFormComponent},
  {path: 'doctor/appointments', component: DoctorAppointmentListComponent},
  {path: 'create-appointment', component: UnregisteredAppointmentFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
