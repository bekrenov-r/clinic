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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LandingPageComponent,
    PatientHomeComponent,
    PatientAppointmentFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    UserModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthorizationHeaderInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
