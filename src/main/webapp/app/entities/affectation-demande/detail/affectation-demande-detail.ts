import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IAffectationDemande } from '../affectation-demande.model';

@Component({
  selector: 'jhi-affectation-demande-detail',
  templateUrl: './affectation-demande-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class AffectationDemandeDetail {
  readonly affectationDemande = input<IAffectationDemande | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
