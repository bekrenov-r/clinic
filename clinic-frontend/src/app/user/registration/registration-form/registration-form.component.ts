import { Component, ElementRef, OnChanges, OnInit, Renderer2, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { PatientRegistration } from 'src/app/models/patient-registration';
import { RegistrationService } from '../registration.service';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RegistrationFormComponent implements OnInit {
  @ViewChild('submitButtonSpinner') submitButtonSpinner: ElementRef;

  step1Form: FormGroup = new FormGroup({});
  step2Form: FormGroup = new FormGroup({});

  step: number = 1;

  static readonly phoneNumberRegex: string = '^\\d{9}$';
  static readonly zipCodeRegex: string = '^\\d{2}-\\d{3}$';
  static readonly peselRegex: string = '^\\d{11}$';

  constructor(
    private formBuilder: FormBuilder,
    private registrationService: RegistrationService,
    private render: Renderer2
  ) { }

  ngOnInit(): void {
    this.step1Form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      pesel: ['', [Validators.required, Validators.pattern(RegistrationFormComponent.peselRegex)]],
      phoneNumber: ['', [Validators.required, Validators.pattern(RegistrationFormComponent.phoneNumberRegex)]],
      email: ['', [Validators.required, Validators.email]],
      personalDataConsent: ['', Validators.requiredTrue],
      address: this.formBuilder.group({
        city: ['', Validators.required],
        street: ['', Validators.required],
        building: ['', Validators.required],
        flat: [''],
        zipCode: ['', [Validators.required, Validators.pattern(RegistrationFormComponent.zipCodeRegex)]]
      })
    });

    this.step2Form = this.formBuilder.group({
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });

    const confirmedPasswordMatchValidator: ValidatorFn =
      (control: AbstractControl): ValidationErrors | null => {
        const password = control.get('password')?.value;
        const confirmPassword = control.get('confirmPassword')?.value;
        return password !== confirmPassword ? { passwordMismatch: true } : null;
      }
    this.step2Form.setValidators(confirmedPasswordMatchValidator);
  }

  onSubmit(): void {
    this.showSpinner();
    this.register();
  }

  register(): void {
    const registration: PatientRegistration = this.composeRegistration();
    this.registrationService.registerPatient(registration)
      .pipe(
        finalize(() => this.hideSpinner())
      ).subscribe({
        next: () => this.step = 3,
        error: () => this.showFailureAlert()
      });
  }

  composeRegistration(): PatientRegistration {
    return {
      firstName: this.step1Form.get('firstName')?.value,
      lastName: this.step1Form.get('lastName')?.value,
      pesel: this.step1Form.get('pesel')?.value,
      email: this.step1Form.get('email')?.value,
      phoneNumber: this.step1Form.get('phoneNumber')?.value,
      address: {
        city: this.step1Form.get('address')?.get('city')?.value,
        street: this.step1Form.get('address')?.get('street')?.value,
        building: this.step1Form.get('address')?.get('building')?.value,
        flat: this.step1Form.get('address')?.get('flat')?.value,
        zipCode: this.step1Form.get('address')?.get('zipCode')?.value
      },
      password: this.step2Form.get('password')?.value
    }
  }

  showFailureAlert(): void {
    const alert: HTMLElement = document.querySelector('#failureAlert');
    this.render.removeClass(alert, 'd-none');
    this.render.addClass(alert, 'd-flex');
  }

  hideFailureAlert(): void {
    const alert: HTMLElement = document.querySelector('#failureAlert');
    this.render.removeClass(alert, 'd-flex');
    this.render.addClass(alert, 'd-none');
  }

  showSpinner(): void {
    this.render.removeClass(this.submitButtonSpinner.nativeElement, 'd-none');
  }

  hideSpinner(): void {
    this.render.addClass(this.submitButtonSpinner.nativeElement, 'd-none');
  }

  getInvalidMsgForEmail(): string {
    return this.step1Form.get('email')?.hasError('required') ? 'Email is required' : 'Please provide valid email';
  }

  getInvalidMsgForPhoneNumber(): string {
    return this.step1Form.get('phoneNumber')?.hasError('required') ? 'Phone number is required' : 'Phone number must consist of 9 digits';
  }

  getInvalidMsgForPesel(): string {
    return this.step1Form.get('pesel')?.hasError('required') ? 'PESEL number is required' : 'Please provide valid PESEL number';
  }

  getInvalidMsgForZipCode(): string {
    return this.step1Form.get('zipCode')?.hasError('required') ? 'Zip code is required' : 'Please provide valid zip code';
  }

  getInvalidMsgForConfirmPassword(): string {
    return this.step2Form.get('confirmPassword')?.hasError('required') ? 'Confirmed password is required' : 'Passwords don\'t match';
  }

  showPasswordMismatchMsg(): boolean {
    const passwordControl: FormControl = this.step2Form.get('password') as FormControl;
    const confirmPasswordControl: FormControl = this.step2Form.get('confirmPassword') as FormControl;
    const noEmptyControls: boolean = !passwordControl.hasError('required') && !confirmPasswordControl.hasError('required');
    return this.step2Form.hasError('passwordMismatch') && confirmPasswordControl.touched && passwordControl.touched && noEmptyControls;
  }
}
