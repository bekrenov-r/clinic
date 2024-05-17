import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import flatpickr from "flatpickr";
import {SpecializationService} from "../../../department/specialization.service";
import {DepartmentService} from "../../../department/department.service";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {Address} from "../../../models/address";
import * as moment from "moment";
import {AppointmentAvailabilityService} from "../../appointment-availability.service";
import {finalize, map, Observable} from "rxjs";
import {DoctorService} from "../../../doctor/doctor.service";
import {AppointmentService} from "../../appointment.service";
import {Router} from "@angular/router";
import {PatientAppointmentRequest} from "../../../models/appointment/patient-appointment-request";

@Component({
  selector: 'app-patient-appointment-form',
  templateUrl: './patient-appointment-form.component.html',
  styleUrls: ['./patient-appointment-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PatientAppointmentFormComponent implements OnInit, AfterViewInit {
  @ViewChild('specialization') specializationSelect: ElementRef;
  @ViewChild('department') departmentSelect: ElementRef;
  @ViewChild('doctor') doctorSelect: ElementRef;
  @ViewChild('date') datePicker: ElementRef;
  @ViewChild('time') timeSelect: ElementRef;
  @ViewChild('submitButtonSpinner') submitButtonSpinner: ElementRef;

  appointmentForm: FormGroup;

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
      time: ['', Validators.required]
    });
  }

  ngAfterViewInit(): void {
    this.populateSpecializations();
  }

  onSubmit(): void {
    const doctorSelectVal = this.appointmentForm.get('doctor').value;
    const appointment: PatientAppointmentRequest = {
      date: moment(this.appointmentForm.get('date').value).format('YYYY-MM-DD'),
      time: this.appointmentForm.get('time').value,
      departmentId: this.appointmentForm.get('department').value,
      anyDoctor: doctorSelectVal === 'any',
      doctorId: doctorSelectVal === 'any' ? null : doctorSelectVal
    }
    this.showSpinner();
    this.appointmentService.createAppointmentAsPatient(appointment)
      .pipe(
        finalize(() => this.hideSpinner())
      )
      .subscribe({
        next: () => this.router.navigate(['/'])
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
}
