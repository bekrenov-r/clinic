import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, ViewEncapsulation} from '@angular/core';
import {Appointment} from "../../../models/appointment/appointment";
import {AppointmentService} from "../../appointment.service";
import {Address} from "../../../models/address";
import {
  logBuilderStatusWarnings
} from "@angular-devkit/build-angular/src/builders/browser-esbuild/builder-status-warnings";

@Component({
  selector: 'app-patient-appointment-list',
  templateUrl: './patient-appointment-list.component.html',
  styleUrls: ['./patient-appointment-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PatientAppointmentListComponent implements AfterViewInit {
  @ViewChild('filters') filters: ElementRef;

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
}
