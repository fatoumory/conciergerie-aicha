import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IMouvementStock, NewMouvementStock } from '../mouvement-stock.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMouvementStock for edit and NewMouvementStockFormGroupInput for create.
 */
type MouvementStockFormGroupInput = IMouvementStock | PartialWithRequiredKeyOf<NewMouvementStock>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMouvementStock | NewMouvementStock> = Omit<T, 'dateTransaction'> & {
  dateTransaction?: string | null;
};

type MouvementStockFormRawValue = FormValueOf<IMouvementStock>;

type NewMouvementStockFormRawValue = FormValueOf<NewMouvementStock>;

type MouvementStockFormDefaults = Pick<NewMouvementStock, 'id' | 'dateTransaction'>;

type MouvementStockFormGroupContent = {
  id: FormControl<MouvementStockFormRawValue['id'] | NewMouvementStock['id']>;
  type: FormControl<MouvementStockFormRawValue['type']>;
  quantite: FormControl<MouvementStockFormRawValue['quantite']>;
  dateTransaction: FormControl<MouvementStockFormRawValue['dateTransaction']>;
  motif: FormControl<MouvementStockFormRawValue['motif']>;
  compteStock: FormControl<MouvementStockFormRawValue['compteStock']>;
};

export type MouvementStockFormGroup = FormGroup<MouvementStockFormGroupContent>;

@Service()
export class MouvementStockFormService {
  createMouvementStockFormGroup(mouvementStock?: MouvementStockFormGroupInput): MouvementStockFormGroup {
    const mouvementStockRawValue = this.convertMouvementStockToMouvementStockRawValue({
      ...this.getFormDefaults(),
      ...(mouvementStock ?? { id: null }),
    });

    return new FormGroup<MouvementStockFormGroupContent>({
      id: new FormControl(
        { value: mouvementStockRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(mouvementStockRawValue.type, {
        validators: [Validators.required],
      }),
      quantite: new FormControl(mouvementStockRawValue.quantite, {
        validators: [Validators.required, Validators.min(0)],
      }),
      dateTransaction: new FormControl(mouvementStockRawValue.dateTransaction, {
        validators: [Validators.required],
      }),
      motif: new FormControl(mouvementStockRawValue.motif),
      compteStock: new FormControl(mouvementStockRawValue.compteStock, {
        validators: [Validators.required],
      }),
    });
  }

  getMouvementStock(form: MouvementStockFormGroup): IMouvementStock | NewMouvementStock {
    return this.convertMouvementStockRawValueToMouvementStock(form.getRawValue());
  }

  resetForm(form: MouvementStockFormGroup, mouvementStock: MouvementStockFormGroupInput): void {
    const mouvementStockRawValue = this.convertMouvementStockToMouvementStockRawValue({ ...this.getFormDefaults(), ...mouvementStock });
    form.reset({
      ...mouvementStockRawValue,
      id: { value: mouvementStockRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): MouvementStockFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateTransaction: currentTime,
    };
  }

  private convertMouvementStockRawValueToMouvementStock(
    rawMouvementStock: MouvementStockFormRawValue | NewMouvementStockFormRawValue,
  ): IMouvementStock | NewMouvementStock {
    return {
      ...rawMouvementStock,
      dateTransaction: dayjs(rawMouvementStock.dateTransaction, DATE_TIME_FORMAT),
    };
  }

  private convertMouvementStockToMouvementStockRawValue(
    mouvementStock: IMouvementStock | (Partial<NewMouvementStock> & MouvementStockFormDefaults),
  ): MouvementStockFormRawValue | PartialWithRequiredKeyOf<NewMouvementStockFormRawValue> {
    return {
      ...mouvementStock,
      dateTransaction: mouvementStock.dateTransaction ? mouvementStock.dateTransaction.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
