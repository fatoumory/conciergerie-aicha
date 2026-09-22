import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IClient } from '../client.model';

@Component({
  selector: 'jhi-client-detail',
  templateUrl: './client-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ClientDetail {
  readonly client = input<IClient | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
