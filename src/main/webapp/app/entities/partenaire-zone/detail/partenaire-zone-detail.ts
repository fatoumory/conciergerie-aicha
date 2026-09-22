import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartenaireZone } from '../partenaire-zone.model';

@Component({
  selector: 'jhi-partenaire-zone-detail',
  templateUrl: './partenaire-zone-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartenaireZoneDetail {
  readonly partenaireZone = input<IPartenaireZone | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
