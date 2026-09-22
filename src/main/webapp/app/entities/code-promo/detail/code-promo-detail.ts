import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICodePromo } from '../code-promo.model';

@Component({
  selector: 'jhi-code-promo-detail',
  templateUrl: './code-promo-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class CodePromoDetail {
  readonly codePromo = input<ICodePromo | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
