import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { StatutDemandeService } from 'app/entities/statut-demande/service/statut-demande.service';
import { IStatutDemande } from 'app/entities/statut-demande/statut-demande.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IHistoriqueStatutDemande } from '../historique-statut-demande.model';
import { HistoriqueStatutDemandeService } from '../service/historique-statut-demande.service';

import { HistoriqueStatutDemandeFormGroup, HistoriqueStatutDemandeFormService } from './historique-statut-demande-form.service';

@Component({
  selector: 'jhi-historique-statut-demande-update',
  templateUrl: './historique-statut-demande-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class HistoriqueStatutDemandeUpdate implements OnInit {
  readonly isSaving = signal(false);
  historiqueStatutDemande: IHistoriqueStatutDemande | null = null;

  demandesSharedCollection = signal<IDemande[]>([]);
  statutDemandesSharedCollection = signal<IStatutDemande[]>([]);

  protected historiqueStatutDemandeService = inject(HistoriqueStatutDemandeService);
  protected historiqueStatutDemandeFormService = inject(HistoriqueStatutDemandeFormService);
  protected demandeService = inject(DemandeService);
  protected statutDemandeService = inject(StatutDemandeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HistoriqueStatutDemandeFormGroup = this.historiqueStatutDemandeFormService.createHistoriqueStatutDemandeFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  compareStatutDemande = (o1: IStatutDemande | null, o2: IStatutDemande | null): boolean =>
    this.statutDemandeService.compareStatutDemande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historiqueStatutDemande }) => {
      this.historiqueStatutDemande = historiqueStatutDemande;
      if (historiqueStatutDemande) {
        this.updateForm(historiqueStatutDemande);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const historiqueStatutDemande = this.historiqueStatutDemandeFormService.getHistoriqueStatutDemande(this.editForm);
    if (historiqueStatutDemande.id === null) {
      this.subscribeToSaveResponse(this.historiqueStatutDemandeService.create(historiqueStatutDemande));
    } else {
      this.subscribeToSaveResponse(this.historiqueStatutDemandeService.update(historiqueStatutDemande));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IHistoriqueStatutDemande | null>): void {
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

  protected updateForm(historiqueStatutDemande: IHistoriqueStatutDemande): void {
    this.historiqueStatutDemande = historiqueStatutDemande;
    this.historiqueStatutDemandeFormService.resetForm(this.editForm, historiqueStatutDemande);

    this.demandesSharedCollection.update(demandes =>
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, historiqueStatutDemande.demande),
    );
    this.statutDemandesSharedCollection.update(statutDemandes =>
      this.statutDemandeService.addStatutDemandeToCollectionIfMissing<IStatutDemande>(statutDemandes, historiqueStatutDemande.statut),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query()
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) =>
          this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.historiqueStatutDemande?.demande),
        ),
      )
      .subscribe((demandes: IDemande[]) => this.demandesSharedCollection.set(demandes));

    this.statutDemandeService
      .query()
      .pipe(map((res: HttpResponse<IStatutDemande[]>) => res.body ?? []))
      .pipe(
        map((statutDemandes: IStatutDemande[]) =>
          this.statutDemandeService.addStatutDemandeToCollectionIfMissing<IStatutDemande>(
            statutDemandes,
            this.historiqueStatutDemande?.statut,
          ),
        ),
      )
      .subscribe((statutDemandes: IStatutDemande[]) => this.statutDemandesSharedCollection.set(statutDemandes));
  }
}
