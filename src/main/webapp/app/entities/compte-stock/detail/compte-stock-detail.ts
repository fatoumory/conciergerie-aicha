import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICompteStock } from '../compte-stock.model';

@Component({
  selector: 'jhi-compte-stock-detail',
  templateUrl: './compte-stock-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CompteStockDetail {
  readonly compteStock = input<ICompteStock | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
