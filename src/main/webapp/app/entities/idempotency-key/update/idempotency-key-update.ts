import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IIdempotencyKey } from '../idempotency-key.model';
import { IdempotencyKeyService } from '../service/idempotency-key.service';

import { IdempotencyKeyFormGroup, IdempotencyKeyFormService } from './idempotency-key-form.service';

@Component({
  selector: 'jhi-idempotency-key-update',
  templateUrl: './idempotency-key-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class IdempotencyKeyUpdate implements OnInit {
  readonly isSaving = signal(false);
  idempotencyKey: IIdempotencyKey | null = null;

  protected idempotencyKeyService = inject(IdempotencyKeyService);
  protected idempotencyKeyFormService = inject(IdempotencyKeyFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IdempotencyKeyFormGroup = this.idempotencyKeyFormService.createIdempotencyKeyFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ idempotencyKey }) => {
      this.idempotencyKey = idempotencyKey;
      if (idempotencyKey) {
        this.updateForm(idempotencyKey);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const idempotencyKey = this.idempotencyKeyFormService.getIdempotencyKey(this.editForm);
    if (idempotencyKey.id === null) {
      this.subscribeToSaveResponse(this.idempotencyKeyService.create(idempotencyKey));
    } else {
      this.subscribeToSaveResponse(this.idempotencyKeyService.update(idempotencyKey));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IIdempotencyKey | null>): void {
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

  protected updateForm(idempotencyKey: IIdempotencyKey): void {
    this.idempotencyKey = idempotencyKey;
    this.idempotencyKeyFormService.resetForm(this.editForm, idempotencyKey);
  }
}
