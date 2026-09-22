import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IEligibiliteService } from 'app/entities/eligibilite-service/eligibilite-service.model';
import { EligibiliteServiceService } from 'app/entities/eligibilite-service/service/eligibilite-service.service';
import { PeriodeQuota } from 'app/entities/enumerations/periode-quota.model';
import { UniteQuota } from 'app/entities/enumerations/unite-quota.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuotaService } from '../quota-service.model';
import { QuotaServiceService } from '../service/quota-service.service';

import { QuotaServiceFormGroup, QuotaServiceFormService } from './quota-service-form.service';

@Component({
  selector: 'jhi-quota-service-update',
  templateUrl: './quota-service-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class QuotaServiceUpdate implements OnInit {
  readonly isSaving = signal(false);
  quotaService: IQuotaService | null = null;
  uniteQuotaValues = Object.keys(UniteQuota);
  periodeQuotaValues = Object.keys(PeriodeQuota);

  eligibiliteServicesSharedCollection = signal<IEligibiliteService[]>([]);

  protected quotaServiceService = inject(QuotaServiceService);
  protected quotaServiceFormService = inject(QuotaServiceFormService);
  protected eligibiliteServiceService = inject(EligibiliteServiceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuotaServiceFormGroup = this.quotaServiceFormService.createQuotaServiceFormGroup();

  compareEligibiliteService = (o1: IEligibiliteService | null, o2: IEligibiliteService | null): boolean =>
    this.eligibiliteServiceService.compareEligibiliteService(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quotaService }) => {
      this.quotaService = quotaService;
      if (quotaService) {
        this.updateForm(quotaService);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quotaService = this.quotaServiceFormService.getQuotaService(this.editForm);
    if (quotaService.id === null) {
      this.subscribeToSaveResponse(this.quotaServiceService.create(quotaService));
    } else {
      this.subscribeToSaveResponse(this.quotaServiceService.update(quotaService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuotaService | null>): void {
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

  protected updateForm(quotaService: IQuotaService): void {
    this.quotaService = quotaService;
    this.quotaServiceFormService.resetForm(this.editForm, quotaService);

    this.eligibiliteServicesSharedCollection.update(eligibiliteServices =>
      this.eligibiliteServiceService.addEligibiliteServiceToCollectionIfMissing<IEligibiliteService>(
        eligibiliteServices,
        quotaService.eligibiliteService,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eligibiliteServiceService
      .query()
      .pipe(map((res: HttpResponse<IEligibiliteService[]>) => res.body ?? []))
      .pipe(
        map((eligibiliteServices: IEligibiliteService[]) =>
          this.eligibiliteServiceService.addEligibiliteServiceToCollectionIfMissing<IEligibiliteService>(
            eligibiliteServices,
            this.quotaService?.eligibiliteService,
          ),
        ),
      )
      .subscribe((eligibiliteServices: IEligibiliteService[]) => this.eligibiliteServicesSharedCollection.set(eligibiliteServices));
  }
}
