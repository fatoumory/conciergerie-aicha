import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { TypeServiceService } from 'app/entities/type-service/service/type-service.service';
import { ITypeService } from 'app/entities/type-service/type-service.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ServiceConciergerieService } from '../service/service-conciergerie.service';
import { IServiceConciergerie } from '../service-conciergerie.model';

import { ServiceConciergerieFormGroup, ServiceConciergerieFormService } from './service-conciergerie-form.service';

@Component({
  selector: 'jhi-service-conciergerie-update',
  templateUrl: './service-conciergerie-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ServiceConciergerieUpdate implements OnInit {
  readonly isSaving = signal(false);
  serviceConciergerie: IServiceConciergerie | null = null;

  typeServicesSharedCollection = signal<ITypeService[]>([]);

  protected serviceConciergerieService = inject(ServiceConciergerieService);
  protected serviceConciergerieFormService = inject(ServiceConciergerieFormService);
  protected typeServiceService = inject(TypeServiceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceConciergerieFormGroup = this.serviceConciergerieFormService.createServiceConciergerieFormGroup();

  compareTypeService = (o1: ITypeService | null, o2: ITypeService | null): boolean => this.typeServiceService.compareTypeService(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceConciergerie }) => {
      this.serviceConciergerie = serviceConciergerie;
      if (serviceConciergerie) {
        this.updateForm(serviceConciergerie);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const serviceConciergerie = this.serviceConciergerieFormService.getServiceConciergerie(this.editForm);
    if (serviceConciergerie.id === null) {
      this.subscribeToSaveResponse(this.serviceConciergerieService.create(serviceConciergerie));
    } else {
      this.subscribeToSaveResponse(this.serviceConciergerieService.update(serviceConciergerie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IServiceConciergerie | null>): void {
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

  protected updateForm(serviceConciergerie: IServiceConciergerie): void {
    this.serviceConciergerie = serviceConciergerie;
    this.serviceConciergerieFormService.resetForm(this.editForm, serviceConciergerie);

    this.typeServicesSharedCollection.update(typeServices =>
      this.typeServiceService.addTypeServiceToCollectionIfMissing<ITypeService>(typeServices, serviceConciergerie.typeService),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeServiceService
      .query()
      .pipe(map((res: HttpResponse<ITypeService[]>) => res.body ?? []))
      .pipe(
        map((typeServices: ITypeService[]) =>
          this.typeServiceService.addTypeServiceToCollectionIfMissing<ITypeService>(typeServices, this.serviceConciergerie?.typeService),
        ),
      )
      .subscribe((typeServices: ITypeService[]) => this.typeServicesSharedCollection.set(typeServices));
  }
}
