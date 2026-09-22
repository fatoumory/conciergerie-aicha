import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IQuotaService } from '../quota-service.model';

@Component({
  selector: 'jhi-quota-service-detail',
  templateUrl: './quota-service-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class QuotaServiceDetail {
  readonly quotaService = input<IQuotaService | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
