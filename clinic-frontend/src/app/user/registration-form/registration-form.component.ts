import { Component, ElementRef, Renderer2, ViewChild, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RegistrationFormComponent {

  constructor(private render: Renderer2) {}

  switchTabs(): void {
    const activeTab: HTMLElement | null = document.querySelector('.registration-step-tab.d-block');
    const hiddenTab: HTMLElement | null = document.querySelector('.registration-step-tab.d-none');
    
    this.render.removeClass(activeTab, 'd-block');
    this.render.addClass(activeTab, 'd-none');
    
    this.render.removeClass(hiddenTab, 'd-none');
    this.render.addClass(hiddenTab, 'd-block');
  }

}
