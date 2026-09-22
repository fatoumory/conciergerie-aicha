import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IQuotaDetail } from 'app/entities/quota-detail/quota-detail.model';
import { QuotaDetailService } from 'app/entities/quota-detail/service/quota-detail.service';
import { IQuotaService } from 'app/entities/quota-service/quota-service.model';
import { QuotaServiceService } from 'app/entities/quota-service/service/quota-service.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IConsommationQuota } from '../consommation-quota.model';
import { ConsommationQuotaService } from '../service/consommation-quota.service';

import { ConsommationQuotaFormGroup, ConsommationQuotaFormService } from './consommation-quota-form.service';

@Component({
  selector: 'jhi-consommation-quota-update',
  templateUrl: './consommation-quota-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ConsommationQuotaUpdate implements OnInit {
  readonly isSaving = signal(false);
  consommationQuota: IConsommationQuota | null = null;

  clientsSharedCollection = signal<IClient[]>([]);
  quotaServicesSharedCollection = signal<IQuotaService[]>([]);
  quotaDetailsSharedCollection = signal<IQuotaDetail[]>([]);

  protected consommationQuotaService = inject(ConsommationQuotaService);
  protected consommationQuotaFormService = inject(ConsommationQuotaFormService);
  protected clientService = inject(ClientService);
  protected quotaServiceService = inject(QuotaServiceService);
  protected quotaDetailService = inject(QuotaDetailService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConsommationQuotaFormGroup = this.consommationQuotaFormService.createConsommationQuotaFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareQuotaService = (o1: IQuotaService | null, o2: IQuotaService | null): boolean =>
    this.quotaServiceService.compareQuotaService(o1, o2);

  compareQuotaDetail = (o1: IQuotaDetail | null, o2: IQuotaDetail | null): boolean => this.quotaDetailService.compareQuotaDetail(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consommationQuota }) => {
      this.consommationQuota = consommationQuota;
      if (consommationQuota) {
        this.updateForm(consommationQuota);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const consommationQuota = this.consommationQuotaFormService.getConsommationQuota(this.editForm);
    if (consommationQuota.id === null) {
      this.subscribeToSaveResponse(this.consommationQuotaService.create(consommationQuota));
    } else {
      this.subscribeToSaveResponse(this.consommationQuotaService.update(consommationQuota));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IConsommationQuota | null>): void {
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

  protected updateForm(consommationQuota: IConsommationQuota): void {
    this.consommationQuota = consommationQuota;
    this.consommationQuotaFormService.resetForm(this.editForm, consommationQuota);

    this.clientsSharedCollection.update(clients =>
      this.clientService.addClientToCollectionIfMissing<IClient>(clients, consommationQuota.client),
    );
    this.quotaServicesSharedCollection.update(quotaServices =>
      this.quotaServiceService.addQuotaServiceToCollectionIfMissing<IQuotaService>(quotaServices, consommationQuota.quotaService),
    );
    this.quotaDetailsSharedCollection.update(quotaDetails =>
      this.quotaDetailService.addQuotaDetailToCollectionIfMissing<IQuotaDetail>(quotaDetails, consommationQuota.quotaDetail),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(
        map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.consommationQuota?.client)),
      )
      .subscribe((clients: IClient[]) => this.clientsSharedCollection.set(clients));

    this.quotaServiceService
      .query()
      .pipe(map((res: HttpResponse<IQuotaService[]>) => res.body ?? []))
      .pipe(
        map((quotaServices: IQuotaService[]) =>
          this.quotaServiceService.addQuotaServiceToCollectionIfMissing<IQuotaService>(quotaServices, this.consommationQuota?.quotaService),
        ),
      )
      .subscribe((quotaServices: IQuotaService[]) => this.quotaServicesSharedCollection.set(quotaServices));

    this.quotaDetailService
      .query()
      .pipe(map((res: HttpResponse<IQuotaDetail[]>) => res.body ?? []))
      .pipe(
        map((quotaDetails: IQuotaDetail[]) =>
          this.quotaDetailService.addQuotaDetailToCollectionIfMissing<IQuotaDetail>(quotaDetails, this.consommationQuota?.quotaDetail),
        ),
      )
      .subscribe((quotaDetails: IQuotaDetail[]) => this.quotaDetailsSharedCollection.set(quotaDetails));
  }
}
