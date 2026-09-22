import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { ModePaiement } from 'app/entities/enumerations/mode-paiement.model';
import { StatutTransaction } from 'app/entities/enumerations/statut-transaction.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TransactionPaiementService } from '../service/transaction-paiement.service';
import { ITransactionPaiement } from '../transaction-paiement.model';

import { TransactionPaiementFormGroup, TransactionPaiementFormService } from './transaction-paiement-form.service';

@Component({
  selector: 'jhi-transaction-paiement-update',
  templateUrl: './transaction-paiement-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TransactionPaiementUpdate implements OnInit {
  readonly isSaving = signal(false);
  transactionPaiement: ITransactionPaiement | null = null;
  modePaiementValues = Object.keys(ModePaiement);
  statutTransactionValues = Object.keys(StatutTransaction);

  demandesSharedCollection = signal<IDemande[]>([]);

  protected transactionPaiementService = inject(TransactionPaiementService);
  protected transactionPaiementFormService = inject(TransactionPaiementFormService);
  protected demandeService = inject(DemandeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionPaiementFormGroup = this.transactionPaiementFormService.createTransactionPaiementFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionPaiement }) => {
      this.transactionPaiement = transactionPaiement;
      if (transactionPaiement) {
        this.updateForm(transactionPaiement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const transactionPaiement = this.transactionPaiementFormService.getTransactionPaiement(this.editForm);
    if (transactionPaiement.id === null) {
      this.subscribeToSaveResponse(this.transactionPaiementService.create(transactionPaiement));
    } else {
      this.subscribeToSaveResponse(this.transactionPaiementService.update(transactionPaiement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITransactionPaiement | null>): void {
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

  protected updateForm(transactionPaiement: ITransactionPaiement): void {
    this.transactionPaiement = transactionPaiement;
    this.transactionPaiementFormService.resetForm(this.editForm, transactionPaiement);

    this.demandesSharedCollection.update(demandes =>
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, transactionPaiement.demande),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query()
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) =>
          this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.transactionPaiement?.demande),
        ),
      )
      .subscribe((demandes: IDemande[]) => this.demandesSharedCollection.set(demandes));
  }
}
