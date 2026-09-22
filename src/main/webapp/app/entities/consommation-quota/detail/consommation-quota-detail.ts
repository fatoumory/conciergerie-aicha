import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IConsommationQuota } from '../consommation-quota.model';

@Component({
  selector: 'jhi-consommation-quota-detail',
  templateUrl: './consommation-quota-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ConsommationQuotaDetail {
  readonly consommationQuota = input<IConsommationQuota | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
