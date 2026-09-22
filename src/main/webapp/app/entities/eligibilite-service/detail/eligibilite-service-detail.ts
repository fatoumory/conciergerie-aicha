import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IEligibiliteService } from '../eligibilite-service.model';

@Component({
  selector: 'jhi-eligibilite-service-detail',
  templateUrl: './eligibilite-service-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class EligibiliteServiceDetail {
  readonly eligibiliteService = input<IEligibiliteService | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
