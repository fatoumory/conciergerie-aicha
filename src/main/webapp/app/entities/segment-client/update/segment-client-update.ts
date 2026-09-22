import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ISegmentClient } from '../segment-client.model';
import { SegmentClientService } from '../service/segment-client.service';

import { SegmentClientFormGroup, SegmentClientFormService } from './segment-client-form.service';

@Component({
  selector: 'jhi-segment-client-update',
  templateUrl: './segment-client-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class SegmentClientUpdate implements OnInit {
  readonly isSaving = signal(false);
  segmentClient: ISegmentClient | null = null;

  protected segmentClientService = inject(SegmentClientService);
  protected segmentClientFormService = inject(SegmentClientFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SegmentClientFormGroup = this.segmentClientFormService.createSegmentClientFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ segmentClient }) => {
      this.segmentClient = segmentClient;
      if (segmentClient) {
        this.updateForm(segmentClient);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const segmentClient = this.segmentClientFormService.getSegmentClient(this.editForm);
    if (segmentClient.id === null) {
      this.subscribeToSaveResponse(this.segmentClientService.create(segmentClient));
    } else {
      this.subscribeToSaveResponse(this.segmentClientService.update(segmentClient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ISegmentClient | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(segmentClient: ISegmentClient): void {
    this.segmentClient = segmentClient;
    this.segmentClientFormService.resetForm(this.editForm, segmentClient);
  }
}
