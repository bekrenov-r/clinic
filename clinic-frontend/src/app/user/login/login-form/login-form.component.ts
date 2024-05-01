import { Component, ElementRef, OnInit, ViewChild, Renderer2 } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {
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
      let email = this.emailControl.value;
      let password = this.passwordControl.value;
      this.authService.authenticate(email, password).subscribe({
        next: () => this.router.navigate(['/home']),
        error: () => this.showAltert()
      });
    }
  }

  getInvalidEmailLabel(): string {
    return this.emailControl.hasError('required') ? 'Email is required' : 'Please provide valid email';
  }

  showAltert(): void {
    const alert = document.querySelector('#invalidCredentialsAlert');
    this.render.removeClass(alert, 'd-none');
    this.render.addClass(alert, 'd-flex');
  }

  hideAlert(): void {
    const alert = document.querySelector('#invalidCredentialsAlert');
    this.render.removeClass(alert, 'd-flex');
    this.render.addClass(alert, 'd-none');
  }
}
