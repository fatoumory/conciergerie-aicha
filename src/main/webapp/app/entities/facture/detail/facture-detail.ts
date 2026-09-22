import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IFacture } from '../facture.model';

@Component({
  selector: 'jhi-facture-detail',
  templateUrl: './facture-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class FactureDetail {
  readonly facture = input<IFacture | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
