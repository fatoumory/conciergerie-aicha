import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TypeClientService } from '../service/type-client.service';
import { ITypeClient } from '../type-client.model';

import { TypeClientFormGroup, TypeClientFormService } from './type-client-form.service';

@Component({
  selector: 'jhi-type-client-update',
  templateUrl: './type-client-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TypeClientUpdate implements OnInit {
  readonly isSaving = signal(false);
  typeClient: ITypeClient | null = null;

  protected typeClientService = inject(TypeClientService);
  protected typeClientFormService = inject(TypeClientFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeClientFormGroup = this.typeClientFormService.createTypeClientFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeClient }) => {
      this.typeClient = typeClient;
      if (typeClient) {
        this.updateForm(typeClient);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const typeClient = this.typeClientFormService.getTypeClient(this.editForm);
    if (typeClient.id === null) {
      this.subscribeToSaveResponse(this.typeClientService.create(typeClient));
    } else {
      this.subscribeToSaveResponse(this.typeClientService.update(typeClient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITypeClient | null>): void {
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

  protected updateForm(typeClient: ITypeClient): void {
    this.typeClient = typeClient;
    this.typeClientFormService.resetForm(this.editForm, typeClient);
  }
}
