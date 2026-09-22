import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TypeDemandeService } from '../service/type-demande.service';
import { ITypeDemande } from '../type-demande.model';

import { TypeDemandeFormGroup, TypeDemandeFormService } from './type-demande-form.service';

@Component({
  selector: 'jhi-type-demande-update',
  templateUrl: './type-demande-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TypeDemandeUpdate implements OnInit {
  readonly isSaving = signal(false);
  typeDemande: ITypeDemande | null = null;

  protected typeDemandeService = inject(TypeDemandeService);
  protected typeDemandeFormService = inject(TypeDemandeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeDemandeFormGroup = this.typeDemandeFormService.createTypeDemandeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeDemande }) => {
      this.typeDemande = typeDemande;
      if (typeDemande) {
        this.updateForm(typeDemande);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const typeDemande = this.typeDemandeFormService.getTypeDemande(this.editForm);
    if (typeDemande.id === null) {
      this.subscribeToSaveResponse(this.typeDemandeService.create(typeDemande));
    } else {
      this.subscribeToSaveResponse(this.typeDemandeService.update(typeDemande));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITypeDemande | null>): void {
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

  protected updateForm(typeDemande: ITypeDemande): void {
    this.typeDemande = typeDemande;
    this.typeDemandeFormService.resetForm(this.editForm, typeDemande);
  }
}
