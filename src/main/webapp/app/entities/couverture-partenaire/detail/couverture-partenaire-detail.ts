import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICouverturePartenaire } from '../couverture-partenaire.model';

@Component({
  selector: 'jhi-couverture-partenaire-detail',
  templateUrl: './couverture-partenaire-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class CouverturePartenaireDetail {
  readonly couverturePartenaire = input<ICouverturePartenaire | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
