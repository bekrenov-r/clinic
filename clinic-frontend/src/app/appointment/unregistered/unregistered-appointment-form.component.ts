import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppointmentAvailabilityService} from "../appointment-availability.service";
import {AppointmentService} from "../appointment.service";
import {SpecializationService} from "../../department/specialization.service";
import {DepartmentService} from "../../department/department.service";
import {DoctorService} from "../../doctor/doctor.service";
import {Router} from "@angular/router";
import * as moment from "moment/moment";
import {finalize, map, Observable} from "rxjs";
import {Address} from "../../models/address";
import flatpickr from "flatpickr";
import {peselRegex, phoneNumberRegex, zipCodeRegex} from "../../models/regex-constants";
import {PatientRegistration} from "../../models/patient";
import Modal from "bootstrap/js/dist/modal";
import * as bootstrap from "bootstrap";
import {PatientAppointmentRequest, UnregisteredPatientAppointmentRequest} from "../../models/appointment";

@Component({
  selector: 'app-unregistered-appointment-form',
  templateUrl: './unregistered-appointment-form.component.html',
  styleUrls: ['./unregistered-appointment-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UnregisteredAppointmentFormComponent implements OnInit, AfterViewInit {
  @ViewChild('specialization') specializationSelect: ElementRef;
  @ViewChild('department') departmentSelect: ElementRef;
  @ViewChild('doctor') doctorSelect: ElementRef;
  @ViewChild('date') datePicker: ElementRef;
  @ViewChild('time') timeSelect: ElementRef;
  @ViewChild('submitButtonSpinner') submitButtonSpinner: ElementRef;
  @ViewChild('successModal') successModal: ElementRef;

  appointmentForm: FormGroup;

  private bsSuccessModal: bootstrap.Modal;

  constructor(
    private availabilityService: AppointmentAvailabilityService,
    private appointmentService: AppointmentService,
    private specializationService: SpecializationService,
    private departmentService: DepartmentService,
    private doctorService: DoctorService,
    private render: Renderer2,
    private formBuilder: FormBuilder,
    private router: Router
  ) {}



  ngOnInit(): void {
    this.initDatePicker();
    this.appointmentForm = this.formBuilder.group({
      specialization: ['', Validators.required],
      department: ['', Validators.required],
      doctor: ['', Validators.required],
      date: ['', Validators.required],
      time: ['', Validators.required],
      personalDataConsent: ['', Validators.requiredTrue],
      personalData: this.formBuilder.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        pesel: ['', [Validators.required, Validators.pattern(peselRegex)]],
        phoneNumber: ['', [Validators.required, Validators.pattern(phoneNumberRegex)]],
        email: ['', [Validators.required, Validators.email]],
      }),
      address: this.formBuilder.group({
        city: ['', Validators.required],
        street: ['', Validators.required],
        building: ['', Validators.required],
        flat: [''],
        zipCode: ['', [Validators.required, Validators.pattern(zipCodeRegex)]]
      })
    });
  }

  ngAfterViewInit(): void {
    this.populateSpecializations();
  }

  onSubmit(): void {
    const doctorSelectVal = this.appointmentForm.get('doctor').value;
    const appointment: UnregisteredPatientAppointmentRequest = {
      date: moment(this.appointmentForm.get('date').value).format('YYYY-MM-DD'),
      time: this.appointmentForm.get('time').value,
      departmentId: this.appointmentForm.get('department').value,
      anyDoctor: doctorSelectVal === 'any',
      doctorId: doctorSelectVal === 'any' ? null : doctorSelectVal,
      patient: this.collectPatientData()
    }
    this.showSpinner();
    this.appointmentService.createAppointmentAsPatient(appointment, false)
      .pipe(
        finalize(() => this.hideSpinner())
      )
      .subscribe({
        next: () => this.showSuccessModal()
      })
  }

  populateDepartments(): void {
    const specSelect: HTMLSelectElement = this.specializationSelect.nativeElement;
    const departmentSelect: HTMLSelectElement = this.departmentSelect.nativeElement;
    this.departmentService.getDepartmentsBySpecialization(specSelect.options[specSelect.selectedIndex].value)
      .subscribe(departments => {
        Array.from(departmentSelect.querySelectorAll('option:not([disabled])'))
          .forEach(option => {
            this.render.removeChild(departmentSelect, option, false);
          });
        departmentSelect.value = '';
        departments.forEach(department => {
          const option = document.createElement('option');
          option.value = String(department.id);
          option.innerText = `${department.name}, ${Address.toSimpleString(department.address)}`;
          this.render.appendChild(departmentSelect, option);
        });
      });
  }

  populateDoctors(): void {
    const departmentSelect: HTMLSelectElement = this.departmentSelect.nativeElement;
    const doctorSelect: HTMLSelectElement = this.doctorSelect.nativeElement;
    const departmentId: number = +departmentSelect.options[departmentSelect.selectedIndex].value;
    this.doctorService.getDoctorsByDepartment(departmentId)
      .subscribe(doctors => {
        this.clearSelectOptions(doctorSelect);
        const anyDoctorOption: HTMLOptionElement = document.createElement('option');
        anyDoctorOption.value = 'any';
        anyDoctorOption.innerText = 'Any doctor';
        this.render.appendChild(doctorSelect, anyDoctorOption);
        doctors.forEach(doctor => {
          const option = document.createElement('option');
          option.value = String(doctor.id);
          option.innerText = `${doctor.firstName} ${doctor.lastName}`;
          this.render.appendChild(doctorSelect, option);
        });
      });
  }

  populateTimes(): void {
    const departmentSelect: HTMLSelectElement = this.departmentSelect.nativeElement;
    const doctorSelect: HTMLSelectElement = this.doctorSelect.nativeElement;
    const timeSelect: HTMLSelectElement = this.timeSelect.nativeElement;
    const date  = moment(this.datePicker.nativeElement.value).format('YYYY-MM-DD');
    let times$: Observable<string[]>;
    if(doctorSelect.value === 'any'){
      let departmentId = +departmentSelect.options[departmentSelect.selectedIndex].value;
      times$ = this.availabilityService.getAvailableTimesByDepartment(departmentId, date)
    } else {
      let doctorId = +doctorSelect.options[doctorSelect.selectedIndex].value;
      times$ = this.availabilityService.getAvailableTimesByDoctor(doctorId, date);
    }
    times$.subscribe(times => {
      this.clearSelectOptions(timeSelect);
      times.forEach(time => {
        const option = document.createElement('option');
        option.value = time;
        option.innerText = time;
        this.render.appendChild(timeSelect, option);
      });
    });
  }

  private initDatePicker(): void {
    this.availabilityService.getAllHolidays()
      .pipe(
        map(
          (dates): number[] => dates.map((date): number => moment(date).valueOf())
        )
      )
      .subscribe((holidays: number[]) => {
        const isDisabled = (date: Date, holidays: number[]): boolean => {
          return moment(date).day() === 6 || moment(date).day() === 0 || holidays.includes(date.getTime());
        }
        let defaultDate = new Date();
        while (isDisabled(defaultDate, holidays)) {
          defaultDate = moment(defaultDate).add(1, 'days').toDate();
        }
        this.appointmentForm.get('date').setValue(defaultDate);
        flatpickr('#date', {
          disable: [
            date => isDisabled(date, holidays)
          ],
          defaultDate: defaultDate,
          dateFormat: 'l, d M Y'
        })
      });
  }

  private clearSelectOptions(select: HTMLSelectElement): void {
    select.value = '';
    Array.from(select.querySelectorAll('option:not([disabled])'))
      .forEach(option => this.render.removeChild(select, option, false));
  }

  private populateSpecializations() {
    const specializationSelect: HTMLElement = this.specializationSelect.nativeElement;
    this.specializationService.getAllSpecializations()
      .subscribe(specializations => {
        Object.keys(specializations).forEach(key => {
          const option = document.createElement('option');
          option.value = key;
          option.innerText = specializations[key];
          this.render.appendChild(specializationSelect, option);
        });
      });
  }

  showSpinner(): void {
    this.render.removeClass(this.submitButtonSpinner.nativeElement, 'd-none');
  }

  hideSpinner(): void {
    this.render.addClass(this.submitButtonSpinner.nativeElement, 'd-none');
  }

  showSuccessModal(): void {
    const modal: HTMLDivElement = this.successModal.nativeElement;
    this.bsSuccessModal = Modal.getOrCreateInstance(modal);
    this.bsSuccessModal.show();
  }

  getInvalidMsgForEmail(): string {
    return this.appointmentForm.get('personalData').get('email').hasError('required') ? 'Email is required' : 'Please provide valid email';
  }

  getInvalidMsgForPhoneNumber(): string {
    return this.appointmentForm.get('personalData').get('phoneNumber').hasError('required') ? 'Phone number is required' : 'Phone number must consist of 9 digits';
  }

  getInvalidMsgForPesel(): string {
    return this.appointmentForm.get('personalData').get('pesel').hasError('required') ? 'PESEL number is required' : 'Please provide valid PESEL number';
  }

  getInvalidMsgForZipCode(): string {
    return this.appointmentForm.get('address').get('zipCode').hasError('required') ? 'Zip code is required' : 'Please provide valid zip code';
  }

  private collectPatientData(): PatientRegistration {
    const personalData = this.appointmentForm.get('personalData');
    const address = this.appointmentForm.get('address');
    return {
      firstName: personalData.get('firstName').value,
      lastName: personalData.get('lastName').value,
      pesel: personalData.get('pesel').value,
      phoneNumber: personalData.get('phoneNumber').value,
      email: personalData.get('email').value,
      address: {
        city: address.get('city').value,
        street: address.get('street').value,
        building: address.get('building').value,
        flat: address.get('flat').value,
        zipCode: address.get('zipCode').value,
      }
    };
  }
}
