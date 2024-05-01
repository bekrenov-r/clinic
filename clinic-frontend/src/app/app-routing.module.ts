import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { LoginFormComponent } from './user/login/login-form/login-form.component';
import { RegistrationFormComponent } from './user/registration/registration-form/registration-form.component';
import { RegistrationConfirmedComponent } from './user/registration/registration-confirmed/registration-confirmed.component';

const routes: Routes = [
  {path: '', component: LandingPageComponent},
  {path: 'login', component: LoginFormComponent},
  {path: 'registration', component: RegistrationFormComponent},
  {path: 'registration/confirmed', component: RegistrationConfirmedComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
