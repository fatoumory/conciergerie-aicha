import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ITransactionPaiement } from '../transaction-paiement.model';

@Component({
  selector: 'jhi-transaction-paiement-detail',
  templateUrl: './transaction-paiement-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class TransactionPaiementDetail {
  readonly transactionPaiement = input<ITransactionPaiement | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
