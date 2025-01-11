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

@Component({
  selector: 'app-department-panel',
  templateUrl: './department-panel.component.html',
  styleUrls: ['./department-panel.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DepartmentPanelComponent implements OnInit {
  @ViewChild('successModal') successModal: ElementRef;

  department: DepartmentDetails = {
    id: 0,
    name: '',
    specialization: '',
    address: new Address('', '', '', '', ''),
    autoConfirmAppointment: false,
    employees: []
  };
  departmentWasChanged: boolean = false;

  private bsSuccessModal: bootstrap.Modal;

  constructor(
    private authService: AuthService,
    private doctorService: DoctorService,
    private departmentService: DepartmentService,
    private router: Router
  ) {}

  ngOnInit() {
    this.requireRole('HEAD_OF_DEPARTMENT');
    this.doctorService.getDoctorProfile().subscribe(doctor =>
      this.departmentService.getDepartmentById(doctor.department.id)
        .subscribe(department => {
          this.department = department;
        })
    );
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
        this.showSuccessModal();
        this.department = res;
        this.departmentWasChanged = false;
      });
  }

  showSuccessModal() {
    const modal: HTMLDivElement = this.successModal.nativeElement;
    this.bsSuccessModal = Modal.getOrCreateInstance(modal);
    this.bsSuccessModal.show();
  }

  private requireRole(role: string) {
    if(!this.authService.userHasRole(role)) {
      this.router.navigate(['/login']);
    }
  }
}
