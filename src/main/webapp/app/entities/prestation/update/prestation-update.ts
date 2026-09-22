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
import { IPrestation } from '../prestation.model';
import { PrestationService } from '../service/prestation.service';

import { PrestationFormGroup, PrestationFormService } from './prestation-form.service';

@Component({
  selector: 'jhi-prestation-update',
  templateUrl: './prestation-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PrestationUpdate implements OnInit {
  readonly isSaving = signal(false);
  prestation: IPrestation | null = null;

  demandesCollection = signal<IDemande[]>([]);
  partenairesSharedCollection = signal<IPartenaire[]>([]);

  protected prestationService = inject(PrestationService);
  protected prestationFormService = inject(PrestationFormService);
  protected demandeService = inject(DemandeService);
  protected partenaireService = inject(PartenaireService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PrestationFormGroup = this.prestationFormService.createPrestationFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  comparePartenaire = (o1: IPartenaire | null, o2: IPartenaire | null): boolean => this.partenaireService.comparePartenaire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestation }) => {
      this.prestation = prestation;
      if (prestation) {
        this.updateForm(prestation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const prestation = this.prestationFormService.getPrestation(this.editForm);
    if (prestation.id === null) {
      this.subscribeToSaveResponse(this.prestationService.create(prestation));
    } else {
      this.subscribeToSaveResponse(this.prestationService.update(prestation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPrestation | null>): void {
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

  protected updateForm(prestation: IPrestation): void {
    this.prestation = prestation;
    this.prestationFormService.resetForm(this.editForm, prestation);

    this.demandesCollection.set(
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(this.demandesCollection(), prestation.demande),
    );
    this.partenairesSharedCollection.update(partenaires =>
      this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, prestation.partenaire),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query({ 'prestationId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) => this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.prestation?.demande)),
      )
      .subscribe((demandes: IDemande[]) => this.demandesCollection.set(demandes));

    this.partenaireService
      .query()
      .pipe(map((res: HttpResponse<IPartenaire[]>) => res.body ?? []))
      .pipe(
        map((partenaires: IPartenaire[]) =>
          this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, this.prestation?.partenaire),
        ),
      )
      .subscribe((partenaires: IPartenaire[]) => this.partenairesSharedCollection.set(partenaires));
  }
}
