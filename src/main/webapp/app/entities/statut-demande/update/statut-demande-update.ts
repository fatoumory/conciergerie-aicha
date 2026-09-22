import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { StatutDemandeService } from '../service/statut-demande.service';
import { IStatutDemande } from '../statut-demande.model';

import { StatutDemandeFormGroup, StatutDemandeFormService } from './statut-demande-form.service';

@Component({
  selector: 'jhi-statut-demande-update',
  templateUrl: './statut-demande-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class StatutDemandeUpdate implements OnInit {
  readonly isSaving = signal(false);
  statutDemande: IStatutDemande | null = null;

  protected statutDemandeService = inject(StatutDemandeService);
  protected statutDemandeFormService = inject(StatutDemandeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StatutDemandeFormGroup = this.statutDemandeFormService.createStatutDemandeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statutDemande }) => {
      this.statutDemande = statutDemande;
      if (statutDemande) {
        this.updateForm(statutDemande);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const statutDemande = this.statutDemandeFormService.getStatutDemande(this.editForm);
    if (statutDemande.id === null) {
      this.subscribeToSaveResponse(this.statutDemandeService.create(statutDemande));
    } else {
      this.subscribeToSaveResponse(this.statutDemandeService.update(statutDemande));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IStatutDemande | null>): void {
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

  protected updateForm(statutDemande: IStatutDemande): void {
    this.statutDemande = statutDemande;
    this.statutDemandeFormService.resetForm(this.editForm, statutDemande);
  }
}
