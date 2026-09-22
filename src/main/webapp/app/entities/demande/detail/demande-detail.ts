import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IDemande } from '../demande.model';

@Component({
  selector: 'jhi-demande-detail',
  templateUrl: './demande-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class DemandeDetail {
  readonly demande = input<IDemande | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
