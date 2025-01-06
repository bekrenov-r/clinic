import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnregisteredAppointmentFormComponent } from './unregistered-appointment-form.component';

describe('UnregisteredAppointmentFormComponent', () => {
  let component: UnregisteredAppointmentFormComponent;
  let fixture: ComponentFixture<UnregisteredAppointmentFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UnregisteredAppointmentFormComponent]
    });
    fixture = TestBed.createComponent(UnregisteredAppointmentFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
