import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ITypeClient } from '../type-client.model';

@Component({
  selector: 'jhi-type-client-detail',
  templateUrl: './type-client-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class TypeClientDetail {
  readonly typeClient = input<ITypeClient | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
