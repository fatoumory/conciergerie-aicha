import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartenaire } from '../partenaire.model';
import { PartenaireService } from '../service/partenaire.service';

import { PartenaireFormGroup, PartenaireFormService } from './partenaire-form.service';

@Component({
  selector: 'jhi-partenaire-update',
  templateUrl: './partenaire-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartenaireUpdate implements OnInit {
  readonly isSaving = signal(false);
  partenaire: IPartenaire | null = null;

  protected partenaireService = inject(PartenaireService);
  protected partenaireFormService = inject(PartenaireFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartenaireFormGroup = this.partenaireFormService.createPartenaireFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partenaire }) => {
      this.partenaire = partenaire;
      if (partenaire) {
        this.updateForm(partenaire);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partenaire = this.partenaireFormService.getPartenaire(this.editForm);
    if (partenaire.id === null) {
      this.subscribeToSaveResponse(this.partenaireService.create(partenaire));
    } else {
      this.subscribeToSaveResponse(this.partenaireService.update(partenaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartenaire | null>): void {
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

  protected updateForm(partenaire: IPartenaire): void {
    this.partenaire = partenaire;
    this.partenaireFormService.resetForm(this.editForm, partenaire);
  }
}
