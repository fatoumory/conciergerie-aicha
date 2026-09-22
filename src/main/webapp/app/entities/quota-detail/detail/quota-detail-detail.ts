import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuotaDetail } from '../quota-detail.model';

@Component({
  selector: 'jhi-quota-detail-detail',
  templateUrl: './quota-detail-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class QuotaDetailDetail {
  readonly quotaDetail = input<IQuotaDetail | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
