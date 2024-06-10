import {AfterViewInit, Component, ElementRef, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import {Appointment} from "../../../models/appointment/appointment";
import {AppointmentService} from "../../appointment.service";
import {Address} from "../../../models/address";
import * as bootstrap from 'bootstrap';
import Modal from 'bootstrap/js/dist/modal';
import {finalize} from "rxjs";

@Component({
  selector: 'app-patient-appointment-list',
  templateUrl: './patient-appointment-list.component.html',
  styleUrls: ['./patient-appointment-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PatientAppointmentListComponent implements AfterViewInit {
  @ViewChild('filters') filters: ElementRef;
  @ViewChild('cancelAppointmentModal') cancelAppointmentModal: ElementRef;

  private bsCancelAppointmentModal: bootstrap.Modal;
  protected readonly Address = Address;

  appointments: Appointment[] = [];

  constructor(private appointmentService: AppointmentService, private render: Renderer2) {}

  ngAfterViewInit(): void {
    this.populateAppointments();
  }

  onFilterOptionChange(): void {
    this.populateAppointments();
  }

  populateAppointments(): void {
    let status: string | undefined;
    const activeFilterOption: HTMLDivElement = this.filters.nativeElement.options[this.filters.nativeElement.selectedIndex];
    switch(activeFilterOption.id){
      case 'all':
        status = undefined;
        break;
      case 'upcoming':
        status = 'CONFIRMED';
        break;
      case 'pending':
        status = 'PENDING';
        break;
      case 'cancelled':
        status = 'CANCELLED';
        break;
      case 'finished':
        status = 'FINISHED';
        break;
    }
    this.appointmentService.getAllAppointments(status)
      .subscribe(appointments => {
        this.appointments = appointments;
      });
  }

  toggleActionsPanel(target: any, cardId: string): void {
    const currentCard: HTMLDivElement = document.querySelector(`#${cardId}`);
    const isCardClosed: boolean = currentCard.classList.contains('closed');
    this.render.removeClass(currentCard, isCardClosed ? 'closed' : 'open');
    this.render.addClass(currentCard, !isCardClosed ? 'closed' : 'open');
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

  getStatusName(status: string): string {
    switch(status) {
      case 'CONFIRMED': return 'UPCOMING';
      case 'CANCELLED': return 'CANCELLED';
      case 'FINISHED': return 'FINISHED';
      case 'PENDING': return 'PENDING';
      default: return null;
    }
  }

  cancelAppointment(): void {
    this.bsCancelAppointmentModal.hide();
    const appointmentId: number = +this.cancelAppointmentModal.nativeElement.getAttribute('data-bs-appointment-id');
    const appointmentCardId: string = `#card-${appointmentId}`;
    this.disableCancelButton(appointmentCardId);
    this.showCancelButtonSpinner(appointmentCardId);
    this.appointmentService.cancelAppointment(appointmentId)
      .pipe(
        finalize(() => this.hideCancelButtonSpinner(appointmentCardId))
      )
      .subscribe(() => this.populateAppointments());
  }

  disableCancelButton(cardId: string) {
    const button: HTMLButtonElement = document.querySelector(`${cardId} .btn-danger`);
    button.disabled = true;
  }

  showCancelButtonSpinner(cardId: string) {
    const spinner: HTMLDivElement = document.querySelector(`${cardId} .spinner-border`);
    this.render.removeClass(spinner, 'd-none');
  }

  hideCancelButtonSpinner(cardId: string) {
    const spinner: HTMLDivElement = document.querySelector(`${cardId} .spinner-border`);
    this.render.addClass(spinner, 'd-none');
  }

  showCancelAppointmentModal(id: number): void {
    const modal: HTMLDivElement = this.cancelAppointmentModal.nativeElement;
    modal.setAttribute('data-bs-appointment-id', String(id));
    this.bsCancelAppointmentModal = Modal.getOrCreateInstance(modal);
    this.bsCancelAppointmentModal.show();
  }
}
