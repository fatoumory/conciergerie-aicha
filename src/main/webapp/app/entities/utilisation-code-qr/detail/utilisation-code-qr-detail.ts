import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IUtilisationCodeQr } from '../utilisation-code-qr.model';

@Component({
  selector: 'jhi-utilisation-code-qr-detail',
  templateUrl: './utilisation-code-qr-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class UtilisationCodeQrDetail {
  readonly utilisationCodeQr = input<IUtilisationCodeQr | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
