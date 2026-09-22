import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartenaire } from '../partenaire.model';

@Component({
  selector: 'jhi-partenaire-detail',
  templateUrl: './partenaire-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartenaireDetail {
  readonly partenaire = input<IPartenaire | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
