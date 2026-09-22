import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IQuotaService } from 'app/entities/quota-service/quota-service.model';
import { QuotaServiceService } from 'app/entities/quota-service/service/quota-service.service';
import { ZoneService } from 'app/entities/zone/service/zone.service';
import { IZone } from 'app/entities/zone/zone.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuotaDetail } from '../quota-detail.model';
import { QuotaDetailService } from '../service/quota-detail.service';

import { QuotaDetailFormGroup, QuotaDetailFormService } from './quota-detail-form.service';

@Component({
  selector: 'jhi-quota-detail-update',
  templateUrl: './quota-detail-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class QuotaDetailUpdate implements OnInit {
  readonly isSaving = signal(false);
  quotaDetail: IQuotaDetail | null = null;

  quotaServicesSharedCollection = signal<IQuotaService[]>([]);
  zonesSharedCollection = signal<IZone[]>([]);

  protected quotaDetailService = inject(QuotaDetailService);
  protected quotaDetailFormService = inject(QuotaDetailFormService);
  protected quotaServiceService = inject(QuotaServiceService);
  protected zoneService = inject(ZoneService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuotaDetailFormGroup = this.quotaDetailFormService.createQuotaDetailFormGroup();

  compareQuotaService = (o1: IQuotaService | null, o2: IQuotaService | null): boolean =>
    this.quotaServiceService.compareQuotaService(o1, o2);

  compareZone = (o1: IZone | null, o2: IZone | null): boolean => this.zoneService.compareZone(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quotaDetail }) => {
      this.quotaDetail = quotaDetail;
      if (quotaDetail) {
        this.updateForm(quotaDetail);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quotaDetail = this.quotaDetailFormService.getQuotaDetail(this.editForm);
    if (quotaDetail.id === null) {
      this.subscribeToSaveResponse(this.quotaDetailService.create(quotaDetail));
    } else {
      this.subscribeToSaveResponse(this.quotaDetailService.update(quotaDetail));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuotaDetail | null>): void {
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

  protected updateForm(quotaDetail: IQuotaDetail): void {
    this.quotaDetail = quotaDetail;
    this.quotaDetailFormService.resetForm(this.editForm, quotaDetail);

    this.quotaServicesSharedCollection.update(quotaServices =>
      this.quotaServiceService.addQuotaServiceToCollectionIfMissing<IQuotaService>(quotaServices, quotaDetail.quotaService),
    );
    this.zonesSharedCollection.update(zones => this.zoneService.addZoneToCollectionIfMissing<IZone>(zones, quotaDetail.zone));
  }

  protected loadRelationshipsOptions(): void {
    this.quotaServiceService
      .query()
      .pipe(map((res: HttpResponse<IQuotaService[]>) => res.body ?? []))
      .pipe(
        map((quotaServices: IQuotaService[]) =>
          this.quotaServiceService.addQuotaServiceToCollectionIfMissing<IQuotaService>(quotaServices, this.quotaDetail?.quotaService),
        ),
      )
      .subscribe((quotaServices: IQuotaService[]) => this.quotaServicesSharedCollection.set(quotaServices));

    this.zoneService
      .query()
      .pipe(map((res: HttpResponse<IZone[]>) => res.body ?? []))
      .pipe(map((zones: IZone[]) => this.zoneService.addZoneToCollectionIfMissing<IZone>(zones, this.quotaDetail?.zone)))
      .subscribe((zones: IZone[]) => this.zonesSharedCollection.set(zones));
  }
}
