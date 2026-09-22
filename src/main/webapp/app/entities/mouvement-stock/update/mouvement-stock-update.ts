import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { ICompteStock } from 'app/entities/compte-stock/compte-stock.model';
import { CompteStockService } from 'app/entities/compte-stock/service/compte-stock.service';
import { TypeMouvementStock } from 'app/entities/enumerations/type-mouvement-stock.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IMouvementStock } from '../mouvement-stock.model';
import { MouvementStockService } from '../service/mouvement-stock.service';

import { MouvementStockFormGroup, MouvementStockFormService } from './mouvement-stock-form.service';

@Component({
  selector: 'jhi-mouvement-stock-update',
  templateUrl: './mouvement-stock-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class MouvementStockUpdate implements OnInit {
  readonly isSaving = signal(false);
  mouvementStock: IMouvementStock | null = null;
  typeMouvementStockValues = Object.keys(TypeMouvementStock);

  compteStocksSharedCollection = signal<ICompteStock[]>([]);

  protected mouvementStockService = inject(MouvementStockService);
  protected mouvementStockFormService = inject(MouvementStockFormService);
  protected compteStockService = inject(CompteStockService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MouvementStockFormGroup = this.mouvementStockFormService.createMouvementStockFormGroup();

  compareCompteStock = (o1: ICompteStock | null, o2: ICompteStock | null): boolean => this.compteStockService.compareCompteStock(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mouvementStock }) => {
      this.mouvementStock = mouvementStock;
      if (mouvementStock) {
        this.updateForm(mouvementStock);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const mouvementStock = this.mouvementStockFormService.getMouvementStock(this.editForm);
    if (mouvementStock.id === null) {
      this.subscribeToSaveResponse(this.mouvementStockService.create(mouvementStock));
    } else {
      this.subscribeToSaveResponse(this.mouvementStockService.update(mouvementStock));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IMouvementStock | null>): void {
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

  protected updateForm(mouvementStock: IMouvementStock): void {
    this.mouvementStock = mouvementStock;
    this.mouvementStockFormService.resetForm(this.editForm, mouvementStock);

    this.compteStocksSharedCollection.update(compteStocks =>
      this.compteStockService.addCompteStockToCollectionIfMissing<ICompteStock>(compteStocks, mouvementStock.compteStock),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.compteStockService
      .query()
      .pipe(map((res: HttpResponse<ICompteStock[]>) => res.body ?? []))
      .pipe(
        map((compteStocks: ICompteStock[]) =>
          this.compteStockService.addCompteStockToCollectionIfMissing<ICompteStock>(compteStocks, this.mouvementStock?.compteStock),
        ),
      )
      .subscribe((compteStocks: ICompteStock[]) => this.compteStocksSharedCollection.set(compteStocks));
  }
}
