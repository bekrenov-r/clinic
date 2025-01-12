import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import * as bootstrap from "bootstrap";
import Modal from "bootstrap/js/dist/modal";

@Component({
  selector: 'success-modal',
  templateUrl: './success-modal.component.html',
  styleUrls: ['./success-modal.component.scss']
})
export class SuccessModalComponent implements OnInit {
  @Input() message: string;
  @Output() hide: EventEmitter<void> = new EventEmitter<void>();

  private _bootstrapModal: bootstrap.Modal;

  ngOnInit() {
    this._bootstrapModal = Modal.getOrCreateInstance('#success-modal');
    document.querySelector('#success-modal')
      .addEventListener('hide.bs.modal', () => this.hide.emit());
  }

  show(message: string) {
    this.message = message;
    this._bootstrapModal.show();
  }
}
