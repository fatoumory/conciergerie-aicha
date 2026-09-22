import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICompteStock } from '../compte-stock.model';
import { CompteStockService } from '../service/compte-stock.service';

import { CompteStockFormGroup, CompteStockFormService } from './compte-stock-form.service';

@Component({
  selector: 'jhi-compte-stock-update',
  templateUrl: './compte-stock-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CompteStockUpdate implements OnInit {
  readonly isSaving = signal(false);
  compteStock: ICompteStock | null = null;

  servicesCollection = signal<IServiceConciergerie[]>([]);

  protected compteStockService = inject(CompteStockService);
  protected compteStockFormService = inject(CompteStockFormService);
  protected serviceConciergerieService = inject(ServiceConciergerieService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CompteStockFormGroup = this.compteStockFormService.createCompteStockFormGroup();

  compareServiceConciergerie = (o1: IServiceConciergerie | null, o2: IServiceConciergerie | null): boolean =>
    this.serviceConciergerieService.compareServiceConciergerie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteStock }) => {
      this.compteStock = compteStock;
      if (compteStock) {
        this.updateForm(compteStock);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const compteStock = this.compteStockFormService.getCompteStock(this.editForm);
    if (compteStock.id === null) {
      this.subscribeToSaveResponse(this.compteStockService.create(compteStock));
    } else {
      this.subscribeToSaveResponse(this.compteStockService.update(compteStock));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICompteStock | null>): void {
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

  protected updateForm(compteStock: ICompteStock): void {
    this.compteStock = compteStock;
    this.compteStockFormService.resetForm(this.editForm, compteStock);

    this.servicesCollection.set(
      this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
        this.servicesCollection(),
        compteStock.service,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.serviceConciergerieService
      .query({ filter: 'comptestock-is-null' })
      .pipe(map((res: HttpResponse<IServiceConciergerie[]>) => res.body ?? []))
      .pipe(
        map((serviceConciergeries: IServiceConciergerie[]) =>
          this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
            serviceConciergeries,
            this.compteStock?.service,
          ),
        ),
      )
      .subscribe((serviceConciergeries: IServiceConciergerie[]) => this.servicesCollection.set(serviceConciergeries));
  }
}
