import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IServiceConciergerie } from '../service-conciergerie.model';

@Component({
  selector: 'jhi-service-conciergerie-detail',
  templateUrl: './service-conciergerie-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ServiceConciergerieDetail {
  readonly serviceConciergerie = input<IServiceConciergerie | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
