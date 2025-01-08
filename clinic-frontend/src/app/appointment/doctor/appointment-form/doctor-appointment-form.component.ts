import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import {AuthService} from "../../../user/login/auth.service";
import {Router} from "@angular/router";
import {AppointmentService} from "../../appointment.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import * as moment from "moment/moment";
import {finalize, map} from "rxjs";
import {environment} from "../../../../environments/environment";
import {jwtDecode} from "jwt-decode";
import {AppointmentAvailabilityService} from "../../appointment-availability.service";
import flatpickr from "flatpickr";
import {PatientService} from "../../../patient/patient.service";
import {peselRegex, phoneNumberRegex, zipCodeRegex} from "../../../models/regex-constants";
import {PersonDto} from "../../../models/person-dto";
import {PatientRegistration} from "../../../models/patient";
import {DoctorAppointmentRequest} from "../../../models/appointment";
import * as bootstrap from "bootstrap";
import Modal from "bootstrap/js/dist/modal";

@Component({
  selector: 'app-appointment-form',
  templateUrl: './doctor-appointment-form.component.html',
  styleUrls: ['./doctor-appointment-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DoctorAppointmentFormComponent implements OnInit, AfterViewInit {
  @ViewChild('time') timeSelect: ElementRef;
  @ViewChild('date') datePicker: ElementRef;
  @ViewChild('peselSearchSuccessAlert') peselSearchSuccessAlert: ElementRef;
  @ViewChild('peselSearchFailureAlert') peselSearchFailureAlert: ElementRef;
  @ViewChild('submitButtonSpinner') spinner: ElementRef;
  @ViewChild('successModal') successModal: ElementRef;

  appointmentForm: FormGroup;
  peselSearchControl: FormControl = new FormControl('');
  existingPatient: PersonDto;
  currentMode: PatientMode;
  bsSuccessModal: bootstrap.Modal

  constructor(
    private authService: AuthService,
    private router: Router,
    private appointmentService: AppointmentService,
    private availabilityService: AppointmentAvailabilityService,
    private patientService: PatientService,
    private formBuilder: FormBuilder,
    private render: Renderer2
  ) {}

  ngOnInit(): void {
    this.currentMode = PatientMode.NEW;
    this.requireRole('DOCTOR');
    this.availabilityService.getAllHolidays()
      .pipe(
        map(
          (dates): number[] => dates.map((date): number => moment(date).valueOf())
        )
      )
      .subscribe((disabledDates: number[]) => {
        this.initDatePicker(disabledDates);
        this.populateTimes();
      });
    this.appointmentForm = this.formBuilder.group({
      date: ['', Validators.required],
      time: ['', Validators.required],
      newPatient: this.formBuilder.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        pesel: ['', [Validators.required, Validators.pattern(peselRegex)]],
        phoneNumber: ['', [Validators.required, Validators.pattern(phoneNumberRegex)]],
        email: ['', [Validators.required, Validators.email]],
        address: this.formBuilder.group({
          city: ['', Validators.required],
          street: ['', Validators.required],
          building: ['', Validators.required],
          flat: [''],
          zipCode: ['', [Validators.required, Validators.pattern(zipCodeRegex)]]
        })
      })
    })
  }

  ngAfterViewInit() {
    Array.from(document.querySelectorAll('.nav-link'))
      .forEach(
        nav => nav.addEventListener('shown.bs.tab', (event: any) => {
          switch(event.target.id) {
            case 'new-patient-tab':
              this.currentMode = PatientMode.NEW;
              break;
            case 'existing-patient-tab':
              this.currentMode = PatientMode.EXISTING;
              break;
          }
        })
      );
  }

  onSubmit() {
    const body: DoctorAppointmentRequest = {
      date: moment(this.appointmentForm.get('date').value).format('YYYY-MM-DD'),
      time: this.appointmentForm.get('time').value
    }
    switch(this.currentMode) {
      case PatientMode.NEW:
        body.patient = this.collectPatientData();
        break;
      case PatientMode.EXISTING:
        body.patientId = this.existingPatient.id;
        break;
    }
    this.showSpinner();
    this.appointmentService.createAppointmentAsDoctor(body)
      .pipe(finalize(() => this.hideSpinner()))
      .subscribe({
        next: () => this.showSuccessModal()
      });
  }

  searchPatient(value: string) {
    this.patientService.getPatientByPesel(value)
      .subscribe({
        next: patient => {
          this.existingPatient = patient;
          this.showAlert(this.peselSearchSuccessAlert);
          this.hideAlert(this.peselSearchFailureAlert);
        },
        error: () => {
          this.existingPatient = null;
          this.showAlert(this.peselSearchFailureAlert);
          this.hideAlert(this.peselSearchSuccessAlert);
        }
      });
  }

  populateTimes(): void {
    const timeSelect: HTMLSelectElement = this.timeSelect.nativeElement;
    const date = moment(this.datePicker.nativeElement.value).format('YYYY-MM-DD');
    this.availabilityService.getAvailableTimesByDoctor(this.getDoctorId(), date)
      .subscribe(times => {
        this.clearSelectOptions(timeSelect);
        times.forEach(time => {
          const option = document.createElement('option');
          option.value = time;
          option.innerText = time;
          this.render.appendChild(timeSelect, option);
        });
      });
  }

  getInvalidMsgForEmail(): string {
    return this.appointmentForm.get('newPatient').get('email').hasError('required')
      ? 'Email is required'
      : 'Please provide valid email';
  }

  getInvalidMsgForPhoneNumber(): string {
    return this.appointmentForm.get('newPatient').get('phoneNumber').hasError('required')
      ? 'Phone number is required'
      : 'Phone number must consist of 9 digits';
  }

  getInvalidMsgForPesel(): string {
    return this.appointmentForm.get('newPatient').get('pesel').hasError('required')
      ? 'PESEL number is required'
      : 'Please provide valid PESEL number';
  }

  getInvalidMsgForZipCode(): string {
    return this.appointmentForm.get('newPatient').get('address').get('zipCode').hasError('required')
      ? 'Zip code is required'
      : 'Please provide valid zip code';
  }

  showSpinner(): void {
    this.render.removeClass(this.spinner.nativeElement, 'd-none');
  }

  hideSpinner(): void {
    this.render.addClass(this.spinner.nativeElement, 'd-none');
  }

  canSubmit(): boolean {
    let patientIsValid: boolean;
    switch (this.currentMode) {
      case PatientMode.NEW:
        patientIsValid = this.appointmentForm.get('newPatient').valid;
        break;
      case PatientMode.EXISTING:
        patientIsValid = !!this.existingPatient;
        break;
      default:
        patientIsValid = false;
    }
    return patientIsValid && this.appointmentForm.get('date').valid && this.appointmentForm.get('time').valid;
  }

  private requireRole(role: string) {
    if (!this.authService.userHasRole(role)) {
      this.router.navigate(['/login']);
    }
  }

  private initDatePicker(disabledDates: number[]) {
    const isDisabled = (date: Date, holidays: number[]): boolean => {
      return moment(date).day() === 6 || moment(date).day() === 0 || holidays.includes(date.getTime());
    }
    let defaultDate = new Date();
    while (isDisabled(defaultDate, disabledDates)) {
      defaultDate = moment(defaultDate).add(1, 'days').toDate();
    }
    this.appointmentForm.get('date').setValue(defaultDate);
    flatpickr('#date', {
      disable: [
        date => isDisabled(date, disabledDates)
      ],
      defaultDate: defaultDate,
      dateFormat: 'l, d M Y'
    });
  }

  private collectPatientData(): PatientRegistration {
    const newPatient = this.appointmentForm.get('newPatient');
    const address = this.appointmentForm.get('newPatient').get('address');
    return {
      firstName: newPatient.get('firstName').value,
      lastName: newPatient.get('lastName').value,
      pesel: newPatient.get('pesel').value,
      phoneNumber: newPatient.get('phoneNumber').value,
      email: newPatient.get('email').value,
      address: {
        city: address.get('city').value,
        street: address.get('street').value,
        building: address.get('building').value,
        flat: address.get('flat').value,
        zipCode: address.get('zipCode').value,
      }
    };
  }

  private clearSelectOptions(select: HTMLSelectElement): void {
    select.value = '';
    Array.from(select.querySelectorAll('option:not([disabled])'))
      .forEach(option => this.render.removeChild(select, option, false));
  }

  private getDoctorId() {
    const jwtPayload: any = jwtDecode(localStorage.getItem(environment.authTokenStorageKey));
    return jwtPayload['doctorId'];
  }

  private showAlert(alert: ElementRef) {
    alert.nativeElement.classList.remove('d-none');
  }

  private hideAlert(alert: ElementRef) {
    alert.nativeElement.classList.add('d-none');
  }

  private showSuccessModal() {
    const modal: HTMLDivElement = this.successModal.nativeElement;
    this.bsSuccessModal = Modal.getOrCreateInstance(modal);
    this.bsSuccessModal.show();
  }
}

enum PatientMode {
  NEW, EXISTING
}
