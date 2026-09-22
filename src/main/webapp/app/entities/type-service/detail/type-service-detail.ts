import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ITypeService } from '../type-service.model';

@Component({
  selector: 'jhi-type-service-detail',
  templateUrl: './type-service-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class TypeServiceDetail {
  readonly typeService = input<ITypeService | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
