import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientHomeComponent } from './patient-home.component';

describe('HomeComponent', () => {
  let component: PatientHomeComponent;
  let fixture: ComponentFixture<PatientHomeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientHomeComponent]
    });
    fixture = TestBed.createComponent(PatientHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
