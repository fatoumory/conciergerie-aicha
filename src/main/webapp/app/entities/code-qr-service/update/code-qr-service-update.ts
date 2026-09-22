import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { StatutCodeQr } from 'app/entities/enumerations/statut-code-qr.model';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { ICodeQrService } from '../code-qr-service.model';
import { CodeQrServiceService } from '../service/code-qr-service.service';

import { CodeQrServiceFormGroup, CodeQrServiceFormService } from './code-qr-service-form.service';

@Component({
  selector: 'jhi-code-qr-service-update',
  templateUrl: './code-qr-service-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CodeQrServiceUpdate implements OnInit {
  readonly isSaving = signal(false);
  codeQrService: ICodeQrService | null = null;
  statutCodeQrValues = Object.keys(StatutCodeQr);

  demandesCollection = signal<IDemande[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected codeQrServiceService = inject(CodeQrServiceService);
  protected codeQrServiceFormService = inject(CodeQrServiceFormService);
  protected demandeService = inject(DemandeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CodeQrServiceFormGroup = this.codeQrServiceFormService.createCodeQrServiceFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeQrService }) => {
      this.codeQrService = codeQrService;
      if (codeQrService) {
        this.updateForm(codeQrService);
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
    const codeQrService = this.codeQrServiceFormService.getCodeQrService(this.editForm);
    if (codeQrService.id === null) {
      this.subscribeToSaveResponse(this.codeQrServiceService.create(codeQrService));
    } else {
      this.subscribeToSaveResponse(this.codeQrServiceService.update(codeQrService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICodeQrService | null>): void {
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

  protected updateForm(codeQrService: ICodeQrService): void {
    this.codeQrService = codeQrService;
    this.codeQrServiceFormService.resetForm(this.editForm, codeQrService);

    this.demandesCollection.set(
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(this.demandesCollection(), codeQrService.demande),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query({ 'codeQrServiceId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) => this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.codeQrService?.demande)),
      )
      .subscribe((demandes: IDemande[]) => this.demandesCollection.set(demandes));
  }
}
