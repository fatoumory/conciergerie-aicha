import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICodeQrService } from 'app/entities/code-qr-service/code-qr-service.model';
import { CodeQrServiceService } from 'app/entities/code-qr-service/service/code-qr-service.service';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { UtilisationCodeQrService } from '../service/utilisation-code-qr.service';
import { IUtilisationCodeQr } from '../utilisation-code-qr.model';

import { UtilisationCodeQrFormGroup, UtilisationCodeQrFormService } from './utilisation-code-qr-form.service';

@Component({
  selector: 'jhi-utilisation-code-qr-update',
  templateUrl: './utilisation-code-qr-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class UtilisationCodeQrUpdate implements OnInit {
  readonly isSaving = signal(false);
  utilisationCodeQr: IUtilisationCodeQr | null = null;

  codeQrServicesSharedCollection = signal<ICodeQrService[]>([]);
  partenairesSharedCollection = signal<IPartenaire[]>([]);

  protected utilisationCodeQrService = inject(UtilisationCodeQrService);
  protected utilisationCodeQrFormService = inject(UtilisationCodeQrFormService);
  protected codeQrServiceService = inject(CodeQrServiceService);
  protected partenaireService = inject(PartenaireService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UtilisationCodeQrFormGroup = this.utilisationCodeQrFormService.createUtilisationCodeQrFormGroup();

  compareCodeQrService = (o1: ICodeQrService | null, o2: ICodeQrService | null): boolean =>
    this.codeQrServiceService.compareCodeQrService(o1, o2);

  comparePartenaire = (o1: IPartenaire | null, o2: IPartenaire | null): boolean => this.partenaireService.comparePartenaire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisationCodeQr }) => {
      this.utilisationCodeQr = utilisationCodeQr;
      if (utilisationCodeQr) {
        this.updateForm(utilisationCodeQr);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const utilisationCodeQr = this.utilisationCodeQrFormService.getUtilisationCodeQr(this.editForm);
    if (utilisationCodeQr.id === null) {
      this.subscribeToSaveResponse(this.utilisationCodeQrService.create(utilisationCodeQr));
    } else {
      this.subscribeToSaveResponse(this.utilisationCodeQrService.update(utilisationCodeQr));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IUtilisationCodeQr | null>): void {
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

  protected updateForm(utilisationCodeQr: IUtilisationCodeQr): void {
    this.utilisationCodeQr = utilisationCodeQr;
    this.utilisationCodeQrFormService.resetForm(this.editForm, utilisationCodeQr);

    this.codeQrServicesSharedCollection.update(codeQrServices =>
      this.codeQrServiceService.addCodeQrServiceToCollectionIfMissing<ICodeQrService>(codeQrServices, utilisationCodeQr.codeQrService),
    );
    this.partenairesSharedCollection.update(partenaires =>
      this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, utilisationCodeQr.partenaire),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.codeQrServiceService
      .query()
      .pipe(map((res: HttpResponse<ICodeQrService[]>) => res.body ?? []))
      .pipe(
        map((codeQrServices: ICodeQrService[]) =>
          this.codeQrServiceService.addCodeQrServiceToCollectionIfMissing<ICodeQrService>(
            codeQrServices,
            this.utilisationCodeQr?.codeQrService,
          ),
        ),
      )
      .subscribe((codeQrServices: ICodeQrService[]) => this.codeQrServicesSharedCollection.set(codeQrServices));

    this.partenaireService
      .query()
      .pipe(map((res: HttpResponse<IPartenaire[]>) => res.body ?? []))
      .pipe(
        map((partenaires: IPartenaire[]) =>
          this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, this.utilisationCodeQr?.partenaire),
        ),
      )
      .subscribe((partenaires: IPartenaire[]) => this.partenairesSharedCollection.set(partenaires));
  }
}
