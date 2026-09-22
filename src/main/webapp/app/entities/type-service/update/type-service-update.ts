import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TypeServiceService } from '../service/type-service.service';
import { ITypeService } from '../type-service.model';

import { TypeServiceFormGroup, TypeServiceFormService } from './type-service-form.service';

@Component({
  selector: 'jhi-type-service-update',
  templateUrl: './type-service-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TypeServiceUpdate implements OnInit {
  readonly isSaving = signal(false);
  typeService: ITypeService | null = null;

  protected typeServiceService = inject(TypeServiceService);
  protected typeServiceFormService = inject(TypeServiceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeServiceFormGroup = this.typeServiceFormService.createTypeServiceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeService }) => {
      this.typeService = typeService;
      if (typeService) {
        this.updateForm(typeService);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const typeService = this.typeServiceFormService.getTypeService(this.editForm);
    if (typeService.id === null) {
      this.subscribeToSaveResponse(this.typeServiceService.create(typeService));
    } else {
      this.subscribeToSaveResponse(this.typeServiceService.update(typeService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITypeService | null>): void {
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

  protected updateForm(typeService: ITypeService): void {
    this.typeService = typeService;
    this.typeServiceFormService.resetForm(this.editForm, typeService);
  }
}
