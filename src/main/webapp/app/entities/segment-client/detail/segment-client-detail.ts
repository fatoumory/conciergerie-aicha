import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ISegmentClient } from '../segment-client.model';

@Component({
  selector: 'jhi-segment-client-detail',
  templateUrl: './segment-client-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class SegmentClientDetail {
  readonly segmentClient = input<ISegmentClient | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
