import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacture } from '../facture.model';
import { FactureService } from '../service/facture.service';

import { FactureFormGroup, FactureFormService } from './facture-form.service';

@Component({
  selector: 'jhi-facture-update',
  templateUrl: './facture-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FactureUpdate implements OnInit {
  readonly isSaving = signal(false);
  facture: IFacture | null = null;

  demandesCollection = signal<IDemande[]>([]);

  protected factureService = inject(FactureService);
  protected factureFormService = inject(FactureFormService);
  protected demandeService = inject(DemandeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactureFormGroup = this.factureFormService.createFactureFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facture }) => {
      this.facture = facture;
      if (facture) {
        this.updateForm(facture);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facture = this.factureFormService.getFacture(this.editForm);
    if (facture.id === null) {
      this.subscribeToSaveResponse(this.factureService.create(facture));
    } else {
      this.subscribeToSaveResponse(this.factureService.update(facture));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacture | null>): void {
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

  protected updateForm(facture: IFacture): void {
    this.facture = facture;
    this.factureFormService.resetForm(this.editForm, facture);

    this.demandesCollection.set(this.demandeService.addDemandeToCollectionIfMissing<IDemande>(this.demandesCollection(), facture.demande));
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query({ 'factureId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(map((demandes: IDemande[]) => this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.facture?.demande)))
      .subscribe((demandes: IDemande[]) => this.demandesCollection.set(demandes));
  }
}
