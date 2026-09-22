import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { IEvaluation } from '../evaluation.model';
import { EvaluationService } from '../service/evaluation.service';

import { EvaluationFormGroup, EvaluationFormService } from './evaluation-form.service';

@Component({
  selector: 'jhi-evaluation-update',
  templateUrl: './evaluation-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class EvaluationUpdate implements OnInit {
  readonly isSaving = signal(false);
  evaluation: IEvaluation | null = null;

  demandesCollection = signal<IDemande[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected evaluationService = inject(EvaluationService);
  protected evaluationFormService = inject(EvaluationFormService);
  protected demandeService = inject(DemandeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EvaluationFormGroup = this.evaluationFormService.createEvaluationFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluation }) => {
      this.evaluation = evaluation;
      if (evaluation) {
        this.updateForm(evaluation);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined, blobType?: BlobType): void {
    this.dataUtils.openFile(base64String, contentType, blobType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertErrorModel>('conciergerieApp.error', { ...err, key: `error.file.${err.key}` }),
        ),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const evaluation = this.evaluationFormService.getEvaluation(this.editForm);
    if (evaluation.id === null) {
      this.subscribeToSaveResponse(this.evaluationService.create(evaluation));
    } else {
      this.subscribeToSaveResponse(this.evaluationService.update(evaluation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEvaluation | null>): void {
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

  protected updateForm(evaluation: IEvaluation): void {
    this.evaluation = evaluation;
    this.evaluationFormService.resetForm(this.editForm, evaluation);

    this.demandesCollection.set(
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(this.demandesCollection(), evaluation.demande),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query({ 'evaluationId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) => this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.evaluation?.demande)),
      )
      .subscribe((demandes: IDemande[]) => this.demandesCollection.set(demandes));
  }
}
