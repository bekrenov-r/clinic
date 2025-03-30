import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { LoginFormComponent } from './user/login/login-form/login-form.component';
import { UserModule } from './user/user.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { PatientHomeComponent } from './home/patient/patient-home.component';
import { PatientAppointmentFormComponent } from './appointment/patient/appointment-form/patient-appointment-form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AuthorizationHeaderInterceptor} from "./user/login/auth.service";
import { PatientAppointmentListComponent } from './appointment/patient/appointment-list/patient-appointment-list.component';
import {NgOptimizedImage} from "@angular/common";
import { AboutComponent } from './landing-page/about/about.component';
import { PatientProfileComponent } from './patient/patient-profile/patient-profile.component';
import { UnregisteredAppointmentFormComponent } from './appointment/unregistered/unregistered-appointment-form.component';
import { DoctorHomeComponent } from './home/doctor/doctor-home.component';
import { DoctorAppointmentFormComponent } from './appointment/doctor/appointment-form/doctor-appointment-form.component';
import { DoctorAppointmentListComponent } from './appointment/doctor/appointment-list/doctor-appointment-list.component';
import { DoctorProfileComponent } from './doctor/profile/doctor-profile.component';
import { DepartmentPipe } from './common/pipes/department.pipe';
import { OccupationPipe } from './common/pipes/occupation.pipe';
import { DepartmentPanelComponent } from './doctor/department-panel/department-panel.component';
import { AddressPipe } from './common/pipes/address.pipe';
import { SuccessModalComponent } from './common/components/success-modal/success-modal.component';
import { AppointmentPageComponent } from './appointment/doctor/appointment-page/appointment-page.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LandingPageComponent,
    PatientHomeComponent,
    PatientAppointmentFormComponent,
    PatientAppointmentListComponent,
    AboutComponent,
    PatientProfileComponent,
    UnregisteredAppointmentFormComponent,
    DoctorHomeComponent,
    DoctorAppointmentFormComponent,
    DoctorAppointmentListComponent,
    DoctorProfileComponent,
    DepartmentPipe,
    OccupationPipe,
    DepartmentPanelComponent,
    AddressPipe,
    SuccessModalComponent,
    AppointmentPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    UserModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    FormsModule
  ],
  providers: [
    AddressPipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthorizationHeaderInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
