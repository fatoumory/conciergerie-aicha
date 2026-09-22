import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICodePromo } from '../code-promo.model';
import { CodePromoService } from '../service/code-promo.service';

import { CodePromoFormGroup, CodePromoFormService } from './code-promo-form.service';

@Component({
  selector: 'jhi-code-promo-update',
  templateUrl: './code-promo-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class CodePromoUpdate implements OnInit {
  readonly isSaving = signal(false);
  codePromo: ICodePromo | null = null;

  protected codePromoService = inject(CodePromoService);
  protected codePromoFormService = inject(CodePromoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CodePromoFormGroup = this.codePromoFormService.createCodePromoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codePromo }) => {
      this.codePromo = codePromo;
      if (codePromo) {
        this.updateForm(codePromo);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const codePromo = this.codePromoFormService.getCodePromo(this.editForm);
    if (codePromo.id === null) {
      this.subscribeToSaveResponse(this.codePromoService.create(codePromo));
    } else {
      this.subscribeToSaveResponse(this.codePromoService.update(codePromo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICodePromo | null>): void {
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

  protected updateForm(codePromo: ICodePromo): void {
    this.codePromo = codePromo;
    this.codePromoFormService.resetForm(this.editForm, codePromo);
  }
}
