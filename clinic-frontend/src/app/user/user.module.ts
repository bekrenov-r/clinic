import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginFormComponent } from './login/login-form/login-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RegistrationFormComponent } from './registration/registration-form/registration-form.component';
import { RegistrationConfirmedComponent } from './registration/registration-confirmed/registration-confirmed.component';



@NgModule({
  declarations: [
    LoginFormComponent,
    RegistrationFormComponent,
    RegistrationConfirmedComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ]
})
export class UserModule { }
