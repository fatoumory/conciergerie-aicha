import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAffectationDemande } from '../affectation-demande.model';
import { AffectationDemandeService } from '../service/affectation-demande.service';

import { AffectationDemandeFormGroup, AffectationDemandeFormService } from './affectation-demande-form.service';

@Component({
  selector: 'jhi-affectation-demande-update',
  templateUrl: './affectation-demande-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class AffectationDemandeUpdate implements OnInit {
  readonly isSaving = signal(false);
  affectationDemande: IAffectationDemande | null = null;

  demandesSharedCollection = signal<IDemande[]>([]);
  partenairesSharedCollection = signal<IPartenaire[]>([]);

  protected affectationDemandeService = inject(AffectationDemandeService);
  protected affectationDemandeFormService = inject(AffectationDemandeFormService);
  protected demandeService = inject(DemandeService);
  protected partenaireService = inject(PartenaireService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AffectationDemandeFormGroup = this.affectationDemandeFormService.createAffectationDemandeFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  comparePartenaire = (o1: IPartenaire | null, o2: IPartenaire | null): boolean => this.partenaireService.comparePartenaire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ affectationDemande }) => {
      this.affectationDemande = affectationDemande;
      if (affectationDemande) {
        this.updateForm(affectationDemande);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const affectationDemande = this.affectationDemandeFormService.getAffectationDemande(this.editForm);
    if (affectationDemande.id === null) {
      this.subscribeToSaveResponse(this.affectationDemandeService.create(affectationDemande));
    } else {
      this.subscribeToSaveResponse(this.affectationDemandeService.update(affectationDemande));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAffectationDemande | null>): void {
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

  protected updateForm(affectationDemande: IAffectationDemande): void {
    this.affectationDemande = affectationDemande;
    this.affectationDemandeFormService.resetForm(this.editForm, affectationDemande);

    this.demandesSharedCollection.update(demandes =>
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, affectationDemande.demande),
    );
    this.partenairesSharedCollection.update(partenaires =>
      this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, affectationDemande.partenaire),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query()
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) =>
          this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.affectationDemande?.demande),
        ),
      )
      .subscribe((demandes: IDemande[]) => this.demandesSharedCollection.set(demandes));

    this.partenaireService
      .query()
      .pipe(map((res: HttpResponse<IPartenaire[]>) => res.body ?? []))
      .pipe(
        map((partenaires: IPartenaire[]) =>
          this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, this.affectationDemande?.partenaire),
        ),
      )
      .subscribe((partenaires: IPartenaire[]) => this.partenairesSharedCollection.set(partenaires));
  }
}
