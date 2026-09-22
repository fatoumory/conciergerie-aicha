import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICouverturePartenaire } from '../couverture-partenaire.model';
import { CouverturePartenaireService } from '../service/couverture-partenaire.service';

import { CouverturePartenaireFormGroup, CouverturePartenaireFormService } from './couverture-partenaire-form.service';

@Component({
  selector: 'jhi-couverture-partenaire-update',
  templateUrl: './couverture-partenaire-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class CouverturePartenaireUpdate implements OnInit {
  readonly isSaving = signal(false);
  couverturePartenaire: ICouverturePartenaire | null = null;

  partenairesSharedCollection = signal<IPartenaire[]>([]);
  serviceConciergeriesSharedCollection = signal<IServiceConciergerie[]>([]);

  protected couverturePartenaireService = inject(CouverturePartenaireService);
  protected couverturePartenaireFormService = inject(CouverturePartenaireFormService);
  protected partenaireService = inject(PartenaireService);
  protected serviceConciergerieService = inject(ServiceConciergerieService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CouverturePartenaireFormGroup = this.couverturePartenaireFormService.createCouverturePartenaireFormGroup();

  comparePartenaire = (o1: IPartenaire | null, o2: IPartenaire | null): boolean => this.partenaireService.comparePartenaire(o1, o2);

  compareServiceConciergerie = (o1: IServiceConciergerie | null, o2: IServiceConciergerie | null): boolean =>
    this.serviceConciergerieService.compareServiceConciergerie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ couverturePartenaire }) => {
      this.couverturePartenaire = couverturePartenaire;
      if (couverturePartenaire) {
        this.updateForm(couverturePartenaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const couverturePartenaire = this.couverturePartenaireFormService.getCouverturePartenaire(this.editForm);
    if (couverturePartenaire.id === null) {
      this.subscribeToSaveResponse(this.couverturePartenaireService.create(couverturePartenaire));
    } else {
      this.subscribeToSaveResponse(this.couverturePartenaireService.update(couverturePartenaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICouverturePartenaire | null>): void {
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

  protected updateForm(couverturePartenaire: ICouverturePartenaire): void {
    this.couverturePartenaire = couverturePartenaire;
    this.couverturePartenaireFormService.resetForm(this.editForm, couverturePartenaire);

    this.partenairesSharedCollection.update(partenaires =>
      this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, couverturePartenaire.partenaire),
    );
    this.serviceConciergeriesSharedCollection.update(serviceConciergeries =>
      this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
        serviceConciergeries,
        couverturePartenaire.service,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partenaireService
      .query()
      .pipe(map((res: HttpResponse<IPartenaire[]>) => res.body ?? []))
      .pipe(
        map((partenaires: IPartenaire[]) =>
          this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, this.couverturePartenaire?.partenaire),
        ),
      )
      .subscribe((partenaires: IPartenaire[]) => this.partenairesSharedCollection.set(partenaires));

    this.serviceConciergerieService
      .query()
      .pipe(map((res: HttpResponse<IServiceConciergerie[]>) => res.body ?? []))
      .pipe(
        map((serviceConciergeries: IServiceConciergerie[]) =>
          this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
            serviceConciergeries,
            this.couverturePartenaire?.service,
          ),
        ),
      )
      .subscribe((serviceConciergeries: IServiceConciergerie[]) => this.serviceConciergeriesSharedCollection.set(serviceConciergeries));
  }
}
