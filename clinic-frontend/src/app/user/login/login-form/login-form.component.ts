import { Component, ElementRef, OnInit, ViewChild, Renderer2 } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import {finalize} from "rxjs";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {
  @ViewChild('submitButtonSpinner') submitButtonSpinner: ElementRef;

  emailControl: FormControl = new FormControl('');
  passwordControl: FormControl = new FormControl('');
  loginForm: FormGroup = new FormGroup({});

  constructor(
    private formBuilder: FormBuilder,
    private render: Renderer2,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.emailControl = new FormControl('', { validators: [Validators.required, Validators.email], updateOn: 'blur' });
    this.passwordControl = new FormControl('', { validators: Validators.required, updateOn: 'blur' });
    this.loginForm = this.formBuilder.group({
      email: this.emailControl,
      password: this.passwordControl
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.showSpinner();
      let email = this.emailControl.value;
      let password = this.passwordControl.value;
      this.authService.authenticate(email, password)
        .pipe(
          finalize(() => this.hideSpinner())
        ).subscribe({
        next: () => this.router.navigate(['/patient/home']),
        error: error => {
          let message = error.status === 403
            ? 'Invalid email or password provided.'
            : 'Something went wrong. Try again later.';
          this.showAlert(message);
        }
      });
    }
  }

  getInvalidEmailLabel(): string {
    return this.emailControl.hasError('required') ? 'Email is required' : 'Please provide valid email';
  }

  showAlert(text: string): void {
    const alert = document.querySelector('#errorAlert');
    const alertText = alert.querySelector('span');
    this.render.setProperty(alertText, 'innerText', text);
    this.render.removeClass(alert, 'd-none');
    this.render.addClass(alert, 'd-flex');
  }

  hideAlert(): void {
    const alert = document.querySelector('#errorAlert');
    this.render.removeClass(alert, 'd-flex');
    this.render.addClass(alert, 'd-none');
  }

  showSpinner(): void {
    this.render.removeClass(this.submitButtonSpinner.nativeElement, 'd-none');
  }

  hideSpinner(): void {
    this.render.addClass(this.submitButtonSpinner.nativeElement, 'd-none');
  }
}
