import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Department, DepartmentDetails, DepartmentRequest} from "../../models/department";
import {AuthService} from "../../user/login/auth.service";
import {DoctorService} from "../doctor.service";
import {DepartmentService} from "../../department/department.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Address} from "../../models/address";
import * as bootstrap from "bootstrap";
import Modal from "bootstrap/js/dist/modal";
import {EmployeeService} from "../employee.service";
import {Employee, EmployeeRequest} from "../../models/employee";
import {peselRegex, phoneNumberRegex, zipCodeRegex} from "../../models/regex-constants";
import {Observable} from "rxjs";

@Component({
  selector: 'app-department-panel',
  templateUrl: './department-panel.component.html',
  styleUrls: ['./department-panel.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DepartmentPanelComponent implements OnInit {
  @ViewChild('successModal') successModal: ElementRef;
  @ViewChild('employeeFormModal') employeeFormModal: ElementRef;

  department: DepartmentDetails = {
    id: 0,
    name: '',
    specialization: '',
    address: new Address('', '', '', '', ''),
    autoConfirmAppointment: false,
    employees: []
  };
  departmentWasChanged: boolean = false;
  employees: Employee[] = [];
  employeeForm: FormGroup;

  private bsSuccessModal: bootstrap.Modal;
  private bsEmployeeFormModal: bootstrap.Modal;

  constructor(
    private authService: AuthService,
    private doctorService: DoctorService,
    private departmentService: DepartmentService,
    private employeeService: EmployeeService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit() {
    this.requireRole('HEAD_OF_DEPARTMENT');
    this.doctorService.getDoctorProfile().subscribe(doctor =>
      this.departmentService.getDepartmentById(doctor.department.id)
        .subscribe(department => {
          this.department = department;
        })
    );
    this.populateEmployees();
    this.employeeForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      pesel: ['', [Validators.required, Validators.pattern(peselRegex)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(phoneNumberRegex)]],
      address: this.formBuilder.group({
        city: ['', Validators.required],
        street: ['', Validators.required],
        building: ['', Validators.required],
        flat: [''],
        zipCode: ['', [Validators.required, Validators.pattern(zipCodeRegex)]]
      }),
      occupation: ['', Validators.required]
    });
  }

  populateEmployees(type: 'current' | 'dismissed' = 'current') {
    (type === 'current' ? this.employeeService.getActiveEmployees() : this.employeeService.getDismissedEmployees())
      .subscribe(res => this.employees = res);
  }

  updateDepartment() {
    const body: DepartmentRequest = {
      name: this.department.name,
      specialization: this.department.specialization,
      autoConfirmAppointment: this.department.autoConfirmAppointment,
      address: this.department.address
    }
    this.departmentService.updateDepartment(this.department.id, body)
      .subscribe(res => {
        this.showSuccessModal('Department was updated');
        this.department = res;
        this.departmentWasChanged = false;
      });
  }

  onEmployeeFormSubmit() {
      this.createEmployee().subscribe(() => {
        this.bsEmployeeFormModal.hide()
        this.populateEmployees();
      });
  }

  private createEmployee(): Observable<any> {
    const address = this.employeeForm.get('address');
    const employee: EmployeeRequest = {
      firstName: this.employeeForm.get('firstName').value,
      lastName: this.employeeForm.get('lastName').value,
      email: this.employeeForm.get('email').value,
      pesel: this.employeeForm.get('pesel').value,
      phoneNumber: this.employeeForm.get('phoneNumber').value,
      address: new Address(
        address.get('city').value,
        address.get('street').value,
        address.get('building').value,
        address.get('flat').value,
        address.get('zipCode').value
      ),
      occupation: this.employeeForm.get('occupation').value
    };
    return this.employeeService.createEmployee(employee);
  }

  showSuccessModal(message: string) {
    const modal: HTMLDivElement = this.successModal.nativeElement;
    modal.querySelector('.modal-body').innerHTML = message;
    this.bsSuccessModal = Modal.getOrCreateInstance(modal);
    this.bsSuccessModal.show();
  }

  openEmployeeFormModal() {
    const modal: HTMLDivElement = this.employeeFormModal.nativeElement;
    this.bsEmployeeFormModal = Modal.getOrCreateInstance(modal);
    this.bsEmployeeFormModal.show();
  }

  onEmployeeTypeChange(select: HTMLSelectElement) {
    const employeeType = select.selectedOptions[0].value as 'current' | 'dismissed';
    this.populateEmployees(employeeType);
  }

  getInvalidMsgForEmail(): string {
    return this.employeeForm.get('email').hasError('required') ? 'Email is required' : 'Please provide valid email';
  }

  getInvalidMsgForPhoneNumber(): string {
    return this.employeeForm.get('phoneNumber').hasError('required') ? 'Phone number is required' : 'Phone number must consist of 9 digits';
  }

  getInvalidMsgForPesel(): string {
    return this.employeeForm.get('pesel').hasError('required') ? 'PESEL number is required' : 'Please provide valid PESEL number';
  }

  getInvalidMsgForZipCode(): string {
    return this.employeeForm.get('address').get('zipCode').hasError('required') ? 'Zip code is required' : 'Please provide valid zip code';
  }

  private requireRole(role: string) {
    if (!this.authService.userHasRole(role)) {
      this.router.navigate(['/login']);
    }
  }
}
