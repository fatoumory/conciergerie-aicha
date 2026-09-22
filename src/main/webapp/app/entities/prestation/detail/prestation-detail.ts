import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPrestation } from '../prestation.model';

@Component({
  selector: 'jhi-prestation-detail',
  templateUrl: './prestation-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class PrestationDetail {
  readonly prestation = input<IPrestation | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
