import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IIdempotencyKey } from '../idempotency-key.model';

@Component({
  selector: 'jhi-idempotency-key-detail',
  templateUrl: './idempotency-key-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class IdempotencyKeyDetail {
  readonly idempotencyKey = input<IIdempotencyKey | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
