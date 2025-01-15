import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import {Appointment} from "../../../models/appointment";
import {AppointmentService} from "../../appointment.service";
import * as bootstrap from "bootstrap";
import Modal from "bootstrap/js/dist/modal";
import {map} from "rxjs";
import * as moment from "moment";
import {SuccessModalComponent} from "../../../common/components/success-modal/success-modal.component";

@Component({
  selector: 'app-appointment-list',
  templateUrl: './doctor-appointment-list.component.html',
  styleUrls: ['./doctor-appointment-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DoctorAppointmentListComponent implements AfterViewInit {
  @ViewChild('filters') filters: ElementRef;
  @ViewChild(SuccessModalComponent) confirmationSuccessModal: SuccessModalComponent;

  appointments: Appointment[] = [];


  constructor(private appointmentService: AppointmentService, private render: Renderer2) {}

  ngAfterViewInit(): void {
    const sessionStorageFilterOption = sessionStorage.getItem('appointments-filter-option');
    if(sessionStorageFilterOption) {
      this.filters.nativeElement.value = sessionStorageFilterOption;
    }
    this.populateAppointments();
  }

  confirmAppointment(appointmentId: number) {
    this.appointmentService.confirmAppointment(appointmentId)
      .subscribe({
        next: () => {
          this.confirmationSuccessModal.show('Appointment confirmed successfully');
          this.populateAppointments();
        }
      });
  }

  onFilterOptionChange() {
    sessionStorage.setItem('appointments-filter-option', this.filters.nativeElement.value);
    this.populateAppointments();
  }

  onTodayCheckboxChange(checkbox: any) {
    if(checkbox.checked) {
      this.appointmentService.getAllAppointments(this.getCurrentStatus())
        .pipe(map(
          (a: Appointment[]) => a.filter((a: Appointment) => moment(a.date).isSame(moment(), 'day'))
        ))
        .subscribe((a: Appointment[]) => this.appointments = a);
    } else {
      this.populateAppointments();
    }
  }

  populateAppointments() {
    this.appointmentService.getAllAppointments(this.getCurrentStatus())
      .subscribe(appointments => {
        this.appointments = appointments;
      });
  }

  getStatusIcon(status: string): string {
    switch(status) {
      case 'CONFIRMED': return 'appointment-upcoming.svg';
      case 'CANCELLED': return 'appointment-cancelled.svg';
      case 'FINISHED': return 'appointment-finished.svg';
      case 'PENDING': return 'appointment-pending.svg';
      default: return null;
    }
  }

  toggleActionsPanel(target: any, cardId: string): void {
    const currentCard: HTMLDivElement = document.querySelector(`#${cardId}`);
    const isCardClosed: boolean = currentCard.classList.contains('closed');
    this.render.removeClass(currentCard, isCardClosed ? 'closed' : 'open');
    this.render.addClass(currentCard, !isCardClosed ? 'closed' : 'open');
  }

  private getCurrentStatus(): string | undefined {
    const selectedFilterOption: HTMLOptionElement = this.filters.nativeElement.selectedOptions[0];
    switch(selectedFilterOption.value){
      case 'all':
        return undefined;
      case 'upcoming':
        return 'CONFIRMED';
      case 'pending':
        return 'PENDING';
      case 'cancelled':
        return 'CANCELLED';
      case 'finished':
        return 'FINISHED';
      default:
        return undefined;
    }
  }
}
