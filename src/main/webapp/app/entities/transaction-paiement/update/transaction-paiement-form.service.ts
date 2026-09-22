import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ITransactionPaiement, NewTransactionPaiement } from '../transaction-paiement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransactionPaiement for edit and NewTransactionPaiementFormGroupInput for create.
 */
type TransactionPaiementFormGroupInput = ITransactionPaiement | PartialWithRequiredKeyOf<NewTransactionPaiement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransactionPaiement | NewTransactionPaiement> = Omit<T, 'dateTransaction'> & {
  dateTransaction?: string | null;
};

type TransactionPaiementFormRawValue = FormValueOf<ITransactionPaiement>;

type NewTransactionPaiementFormRawValue = FormValueOf<NewTransactionPaiement>;

type TransactionPaiementFormDefaults = Pick<NewTransactionPaiement, 'id' | 'dateTransaction'>;

type TransactionPaiementFormGroupContent = {
  id: FormControl<TransactionPaiementFormRawValue['id'] | NewTransactionPaiement['id']>;
  montant: FormControl<TransactionPaiementFormRawValue['montant']>;
  modePaiement: FormControl<TransactionPaiementFormRawValue['modePaiement']>;
  statut: FormControl<TransactionPaiementFormRawValue['statut']>;
  referenceExterne: FormControl<TransactionPaiementFormRawValue['referenceExterne']>;
  dateTransaction: FormControl<TransactionPaiementFormRawValue['dateTransaction']>;
  demande: FormControl<TransactionPaiementFormRawValue['demande']>;
};

export type TransactionPaiementFormGroup = FormGroup<TransactionPaiementFormGroupContent>;

@Service()
export class TransactionPaiementFormService {
  createTransactionPaiementFormGroup(transactionPaiement?: TransactionPaiementFormGroupInput): TransactionPaiementFormGroup {
    const transactionPaiementRawValue = this.convertTransactionPaiementToTransactionPaiementRawValue({
      ...this.getFormDefaults(),
      ...(transactionPaiement ?? { id: null }),
    });

    return new FormGroup<TransactionPaiementFormGroupContent>({
      id: new FormControl(
        { value: transactionPaiementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      montant: new FormControl(transactionPaiementRawValue.montant, {
        validators: [Validators.required, Validators.min(0)],
      }),
      modePaiement: new FormControl(transactionPaiementRawValue.modePaiement, {
        validators: [Validators.required],
      }),
      statut: new FormControl(transactionPaiementRawValue.statut, {
        validators: [Validators.required],
      }),
      referenceExterne: new FormControl(transactionPaiementRawValue.referenceExterne),
      dateTransaction: new FormControl(transactionPaiementRawValue.dateTransaction, {
        validators: [Validators.required],
      }),
      demande: new FormControl(transactionPaiementRawValue.demande, {
        validators: [Validators.required],
      }),
    });
  }

  getTransactionPaiement(form: TransactionPaiementFormGroup): ITransactionPaiement | NewTransactionPaiement {
    return this.convertTransactionPaiementRawValueToTransactionPaiement(form.getRawValue());
  }

  resetForm(form: TransactionPaiementFormGroup, transactionPaiement: TransactionPaiementFormGroupInput): void {
    const transactionPaiementRawValue = this.convertTransactionPaiementToTransactionPaiementRawValue({
      ...this.getFormDefaults(),
      ...transactionPaiement,
    });
    form.reset({
      ...transactionPaiementRawValue,
      id: { value: transactionPaiementRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TransactionPaiementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateTransaction: currentTime,
    };
  }

  private convertTransactionPaiementRawValueToTransactionPaiement(
    rawTransactionPaiement: TransactionPaiementFormRawValue | NewTransactionPaiementFormRawValue,
  ): ITransactionPaiement | NewTransactionPaiement {
    return {
      ...rawTransactionPaiement,
      dateTransaction: dayjs(rawTransactionPaiement.dateTransaction, DATE_TIME_FORMAT),
    };
  }

  private convertTransactionPaiementToTransactionPaiementRawValue(
    transactionPaiement: ITransactionPaiement | (Partial<NewTransactionPaiement> & TransactionPaiementFormDefaults),
  ): TransactionPaiementFormRawValue | PartialWithRequiredKeyOf<NewTransactionPaiementFormRawValue> {
    return {
      ...transactionPaiement,
      dateTransaction: transactionPaiement.dateTransaction ? transactionPaiement.dateTransaction.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
