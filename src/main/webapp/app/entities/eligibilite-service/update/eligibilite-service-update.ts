import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { ISegmentClient } from 'app/entities/segment-client/segment-client.model';
import { SegmentClientService } from 'app/entities/segment-client/service/segment-client.service';
import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { TypeClientService } from 'app/entities/type-client/service/type-client.service';
import { ITypeClient } from 'app/entities/type-client/type-client.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IEligibiliteService } from '../eligibilite-service.model';
import { EligibiliteServiceService } from '../service/eligibilite-service.service';

import { EligibiliteServiceFormGroup, EligibiliteServiceFormService } from './eligibilite-service-form.service';

@Component({
  selector: 'jhi-eligibilite-service-update',
  templateUrl: './eligibilite-service-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class EligibiliteServiceUpdate implements OnInit {
  readonly isSaving = signal(false);
  eligibiliteService: IEligibiliteService | null = null;

  serviceConciergeriesSharedCollection = signal<IServiceConciergerie[]>([]);
  segmentClientsSharedCollection = signal<ISegmentClient[]>([]);
  typeClientsSharedCollection = signal<ITypeClient[]>([]);

  protected eligibiliteServiceService = inject(EligibiliteServiceService);
  protected eligibiliteServiceFormService = inject(EligibiliteServiceFormService);
  protected serviceConciergerieService = inject(ServiceConciergerieService);
  protected segmentClientService = inject(SegmentClientService);
  protected typeClientService = inject(TypeClientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EligibiliteServiceFormGroup = this.eligibiliteServiceFormService.createEligibiliteServiceFormGroup();

  compareServiceConciergerie = (o1: IServiceConciergerie | null, o2: IServiceConciergerie | null): boolean =>
    this.serviceConciergerieService.compareServiceConciergerie(o1, o2);

  compareSegmentClient = (o1: ISegmentClient | null, o2: ISegmentClient | null): boolean =>
    this.segmentClientService.compareSegmentClient(o1, o2);

  compareTypeClient = (o1: ITypeClient | null, o2: ITypeClient | null): boolean => this.typeClientService.compareTypeClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eligibiliteService }) => {
      this.eligibiliteService = eligibiliteService;
      if (eligibiliteService) {
        this.updateForm(eligibiliteService);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const eligibiliteService = this.eligibiliteServiceFormService.getEligibiliteService(this.editForm);
    if (eligibiliteService.id === null) {
      this.subscribeToSaveResponse(this.eligibiliteServiceService.create(eligibiliteService));
    } else {
      this.subscribeToSaveResponse(this.eligibiliteServiceService.update(eligibiliteService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEligibiliteService | null>): void {
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

  protected updateForm(eligibiliteService: IEligibiliteService): void {
    this.eligibiliteService = eligibiliteService;
    this.eligibiliteServiceFormService.resetForm(this.editForm, eligibiliteService);

    this.serviceConciergeriesSharedCollection.update(serviceConciergeries =>
      this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
        serviceConciergeries,
        eligibiliteService.service,
      ),
    );
    this.segmentClientsSharedCollection.update(segmentClients =>
      this.segmentClientService.addSegmentClientToCollectionIfMissing<ISegmentClient>(segmentClients, eligibiliteService.segmentClient),
    );
    this.typeClientsSharedCollection.update(typeClients =>
      this.typeClientService.addTypeClientToCollectionIfMissing<ITypeClient>(typeClients, eligibiliteService.typeClient),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.serviceConciergerieService
      .query()
      .pipe(map((res: HttpResponse<IServiceConciergerie[]>) => res.body ?? []))
      .pipe(
        map((serviceConciergeries: IServiceConciergerie[]) =>
          this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
            serviceConciergeries,
            this.eligibiliteService?.service,
          ),
        ),
      )
      .subscribe((serviceConciergeries: IServiceConciergerie[]) => this.serviceConciergeriesSharedCollection.set(serviceConciergeries));

    this.segmentClientService
      .query()
      .pipe(map((res: HttpResponse<ISegmentClient[]>) => res.body ?? []))
      .pipe(
        map((segmentClients: ISegmentClient[]) =>
          this.segmentClientService.addSegmentClientToCollectionIfMissing<ISegmentClient>(
            segmentClients,
            this.eligibiliteService?.segmentClient,
          ),
        ),
      )
      .subscribe((segmentClients: ISegmentClient[]) => this.segmentClientsSharedCollection.set(segmentClients));

    this.typeClientService
      .query()
      .pipe(map((res: HttpResponse<ITypeClient[]>) => res.body ?? []))
      .pipe(
        map((typeClients: ITypeClient[]) =>
          this.typeClientService.addTypeClientToCollectionIfMissing<ITypeClient>(typeClients, this.eligibiliteService?.typeClient),
        ),
      )
      .subscribe((typeClients: ITypeClient[]) => this.typeClientsSharedCollection.set(typeClients));
  }
}
