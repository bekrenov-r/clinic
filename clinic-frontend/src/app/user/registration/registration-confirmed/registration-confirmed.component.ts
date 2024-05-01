import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { RegistrationService } from '../registration.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../login/auth.service';
import { environment } from 'src/environments/environment';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-registration-confirmed',
  templateUrl: './registration-confirmed.component.html',
  styleUrls: ['./registration-confirmed.component.scss']
})
export class RegistrationConfirmedComponent {
  @ViewChild('fallbackContent') fallbackContent: ElementRef;
  @ViewChild('mainContent') mainContent: ElementRef;
  @ViewChild('spinner') spinner: ElementRef;

  registrationConfirmed: boolean;

  constructor(
    private registrationService: RegistrationService, 
    private route: ActivatedRoute,
    private render: Renderer2
    ) {}

  ngOnInit(): void {
    let token: string = this.route.snapshot.queryParams['token'];
    this.registrationService.activateAccount(token)
      .pipe(
        finalize(() => this.hideSpinner())
      ).subscribe({
        next: token => {
          this.showMainContent();
          localStorage.setItem(environment.authTokenStorageKey, token);
        },
        error: () => this.showFallbackContent()
      });
  }

  showMainContent(): void {
    this.render.removeClass(this.mainContent.nativeElement, 'd-none');
  }

  showFallbackContent(): void {
    this.render.removeClass(this.fallbackContent.nativeElement, 'd-none');
  }

  hideSpinner(): void {
    this.render.addClass(this.spinner.nativeElement, 'd-none');
  }
}
