import { Component, ElementRef, OnInit, ViewChild, Renderer2 } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {
  emailControl: FormControl = new FormControl('');
  passwordControl: FormControl = new FormControl('');
  loginForm: FormGroup = new FormGroup({});

  @ViewChild('emailInput') emailInputRef?: ElementRef;
  @ViewChild('emailLabel') emailLabelRef?: ElementRef;
  @ViewChild('passwordInput') passwordInputRef?: ElementRef;
  @ViewChild('passwordLabel') passwordLabelRef?: ElementRef;

  constructor(
    private formBuilder: FormBuilder,
    private render: Renderer2,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.emailControl = new FormControl('', { validators: [Validators.required, Validators.email], updateOn: 'blur' });
    this.passwordControl = new FormControl('', { validators: Validators.required, updateOn: 'blur' });
    this.loginForm = this.formBuilder.group({
      email: this.emailControl,
      password: this.passwordControl
    });
    this.emailControl.statusChanges.subscribe(() => this.validateEmail());
    this.passwordControl.statusChanges.subscribe(() => this.validatePassword());
    
    document.querySelector('form')?.addEventListener('submit', event => {
      if(this.loginForm.invalid){
        event.preventDefault();
        event.stopPropagation();
        this.validateEmail();
        this.validatePassword();
      }
    })
  }

  onSubmit(): void {
    let email = this.emailControl.value;
    let password = this.passwordControl.value;
    this.authService.authenticate(email, password).subscribe(res => console.log(res));
  }

  validateEmail(): void {
    const emailInput: HTMLElement = this.emailInputRef?.nativeElement;
    const emailLabel: HTMLElement = this.emailLabelRef?.nativeElement;
    if (this.emailControl.invalid) {
      const invalidMsg = this.emailControl.hasError('required') ? 'Email is required' : 'Please provide valid email';
      this.makeInputInvalid(emailInput, emailLabel, invalidMsg);
    } else {
      this.makeInputValid(emailInput, emailLabel, 'Email');
    }
  }

  validatePassword(): void {
    const passwordInput: HTMLElement = this.passwordInputRef?.nativeElement;
    const passwordLabel: HTMLElement = this.passwordLabelRef?.nativeElement;
    if (this.passwordControl.invalid) {
      this.makeInputInvalid(passwordInput, passwordLabel, 'Password is required');
    } else {
      this.makeInputValid(passwordInput, passwordLabel, 'Password');
    }
  }

  makeInputInvalid(input: HTMLElement, label: HTMLElement, invalidMsg: string): void {
    this.render.addClass(input, 'is-invalid');
    this.render.addClass(label, 'invalid');
    this.render.setProperty(label, 'innerText', invalidMsg);
  }

  makeInputValid(input: HTMLElement, label: HTMLElement, validMsg: string): void {
    this.render.removeClass(input, 'is-invalid');
    this.render.removeClass(label, 'invalid');
    this.render.setProperty(label, 'innerText', validMsg);
  }
}
