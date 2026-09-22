import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IStatutDemande } from '../statut-demande.model';

@Component({
  selector: 'jhi-statut-demande-detail',
  templateUrl: './statut-demande-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class StatutDemandeDetail {
  readonly statutDemande = input<IStatutDemande | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
