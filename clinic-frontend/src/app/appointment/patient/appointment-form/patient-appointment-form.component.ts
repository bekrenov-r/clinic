import {Component, Renderer2, ViewEncapsulation} from '@angular/core';
import flatpickr from "flatpickr";
import {SpecializationService} from "../../../department/specialization.service";
import {DepartmentService} from "../../../department/department.service";
import {FormGroup} from "@angular/forms";
import {Address} from "../../../models/address";
import * as moment from "moment";
import {AppointmentAvailabilityService} from "../../appointment-availability.service";
import {map, Observable} from "rxjs";
import {DoctorService} from "../../../doctor/doctor.service";

@Component({
  selector: 'app-patient-appointment-form',
  templateUrl: './patient-appointment-form.component.html',
  styleUrls: ['./patient-appointment-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PatientAppointmentFormComponent {
  appointmentForm: FormGroup;

  constructor(
    private availabilityService: AppointmentAvailabilityService,
    private specializationService: SpecializationService,
    private departmentService: DepartmentService,
    private doctorService: DoctorService,
    private render: Renderer2
    ) {}

  ngOnInit(): void {
    this.initDatePicker();
    this.populateSpecializations();
  }

  populateDepartments(): void {
    const specSelect: HTMLSelectElement = document.querySelector('#specialization');
    const departmentSelect: HTMLSelectElement = document.querySelector('#department');
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
    const departmentSelect: HTMLSelectElement = document.querySelector('#department');
    const doctorSelect: HTMLSelectElement = document.querySelector('#doctor');
    this.doctorService.getDoctorsByDepartment(+departmentSelect.options[departmentSelect.selectedIndex].value)
      .subscribe(doctors => {
        this.clearSelectOptions(doctorSelect);
        doctors.forEach(doctor => {
          const option = document.createElement('option');
          option.value = String(doctor.id);
          option.innerText = `${doctor.firstName} ${doctor.lastName}`;
          this.render.appendChild(doctorSelect, option);
        });
      });
  }

  populateTimes(): void {
    const departmentSelect: HTMLSelectElement = document.querySelector('#department');
    const doctorSelect: HTMLSelectElement = document.querySelector('#doctor');
    const timeSelect: HTMLSelectElement = document.querySelector('#time');
    const date  = moment((document.querySelector('#date') as HTMLInputElement).value).format('YYYY-MM-DD');
    let times$: Observable<string[]>;
    if(doctorSelect.value === ''){
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

  onCheckboxChange(checkbox: any) {
    const doctorSelect: any = document.querySelector('#doctor');
    doctorSelect.disabled = checkbox.checked;
    if(checkbox.checked){
      this.clearSelectOptions(doctorSelect);
    } else {
      this.populateDoctors();
    }
    this.clearSelectOptions(document.querySelector('#time'));
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
    const specializationSelect: HTMLElement = document.querySelector('#specialization');
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
}
