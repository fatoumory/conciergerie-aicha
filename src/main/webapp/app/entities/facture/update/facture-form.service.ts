import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IFacture, NewFacture } from '../facture.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacture for edit and NewFactureFormGroupInput for create.
 */
type FactureFormGroupInput = IFacture | PartialWithRequiredKeyOf<NewFacture>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFacture | NewFacture> = Omit<T, 'dateGeneration'> & {
  dateGeneration?: string | null;
};

type FactureFormRawValue = FormValueOf<IFacture>;

type NewFactureFormRawValue = FormValueOf<NewFacture>;

type FactureFormDefaults = Pick<NewFacture, 'id' | 'dateGeneration'>;

type FactureFormGroupContent = {
  id: FormControl<FactureFormRawValue['id'] | NewFacture['id']>;
  numero: FormControl<FactureFormRawValue['numero']>;
  montant: FormControl<FactureFormRawValue['montant']>;
  dateGeneration: FormControl<FactureFormRawValue['dateGeneration']>;
  demande: FormControl<FactureFormRawValue['demande']>;
};

export type FactureFormGroup = FormGroup<FactureFormGroupContent>;

@Service()
export class FactureFormService {
  createFactureFormGroup(facture?: FactureFormGroupInput): FactureFormGroup {
    const factureRawValue = this.convertFactureToFactureRawValue({
      ...this.getFormDefaults(),
      ...(facture ?? { id: null }),
    });

    return new FormGroup<FactureFormGroupContent>({
      id: new FormControl(
        { value: factureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: new FormControl(factureRawValue.numero, {
        validators: [Validators.required],
      }),
      montant: new FormControl(factureRawValue.montant, {
        validators: [Validators.required, Validators.min(0)],
      }),
      dateGeneration: new FormControl(factureRawValue.dateGeneration, {
        validators: [Validators.required],
      }),
      demande: new FormControl(factureRawValue.demande, {
        validators: [Validators.required],
      }),
    });
  }

  getFacture(form: FactureFormGroup): IFacture | NewFacture {
    return this.convertFactureRawValueToFacture(form.getRawValue());
  }

  resetForm(form: FactureFormGroup, facture: FactureFormGroupInput): void {
    const factureRawValue = this.convertFactureToFactureRawValue({ ...this.getFormDefaults(), ...facture });
    form.reset({
      ...factureRawValue,
      id: { value: factureRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FactureFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateGeneration: currentTime,
    };
  }

  private convertFactureRawValueToFacture(rawFacture: FactureFormRawValue | NewFactureFormRawValue): IFacture | NewFacture {
    return {
      ...rawFacture,
      dateGeneration: dayjs(rawFacture.dateGeneration, DATE_TIME_FORMAT),
    };
  }

  private convertFactureToFactureRawValue(
    facture: IFacture | (Partial<NewFacture> & FactureFormDefaults),
  ): FactureFormRawValue | PartialWithRequiredKeyOf<NewFactureFormRawValue> {
    return {
      ...facture,
      dateGeneration: facture.dateGeneration ? facture.dateGeneration.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
