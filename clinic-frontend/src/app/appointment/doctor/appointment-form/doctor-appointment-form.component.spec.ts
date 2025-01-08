import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorAppointmentFormComponent } from './doctor-appointment-form.component';

describe('DoctorAppointmentFormComponent', () => {
  let component: DoctorAppointmentFormComponent;
  let fixture: ComponentFixture<DoctorAppointmentFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoctorAppointmentFormComponent]
    });
    fixture = TestBed.createComponent(DoctorAppointmentFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
