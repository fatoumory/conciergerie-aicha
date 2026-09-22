import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IPrestation, NewPrestation } from '../prestation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrestation for edit and NewPrestationFormGroupInput for create.
 */
type PrestationFormGroupInput = IPrestation | PartialWithRequiredKeyOf<NewPrestation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPrestation | NewPrestation> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

type PrestationFormRawValue = FormValueOf<IPrestation>;

type NewPrestationFormRawValue = FormValueOf<NewPrestation>;

type PrestationFormDefaults = Pick<NewPrestation, 'id' | 'dateDebut' | 'dateFin'>;

type PrestationFormGroupContent = {
  id: FormControl<PrestationFormRawValue['id'] | NewPrestation['id']>;
  dateDebut: FormControl<PrestationFormRawValue['dateDebut']>;
  dateFin: FormControl<PrestationFormRawValue['dateFin']>;
  demande: FormControl<PrestationFormRawValue['demande']>;
  partenaire: FormControl<PrestationFormRawValue['partenaire']>;
};

export type PrestationFormGroup = FormGroup<PrestationFormGroupContent>;

@Service()
export class PrestationFormService {
  createPrestationFormGroup(prestation?: PrestationFormGroupInput): PrestationFormGroup {
    const prestationRawValue = this.convertPrestationToPrestationRawValue({
      ...this.getFormDefaults(),
      ...(prestation ?? { id: null }),
    });

    return new FormGroup<PrestationFormGroupContent>({
      id: new FormControl(
        { value: prestationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDebut: new FormControl(prestationRawValue.dateDebut),
      dateFin: new FormControl(prestationRawValue.dateFin),
      demande: new FormControl(prestationRawValue.demande, {
        validators: [Validators.required],
      }),
      partenaire: new FormControl(prestationRawValue.partenaire, {
        validators: [Validators.required],
      }),
    });
  }

  getPrestation(form: PrestationFormGroup): IPrestation | NewPrestation {
    return this.convertPrestationRawValueToPrestation(form.getRawValue());
  }

  resetForm(form: PrestationFormGroup, prestation: PrestationFormGroupInput): void {
    const prestationRawValue = this.convertPrestationToPrestationRawValue({ ...this.getFormDefaults(), ...prestation });
    form.reset({
      ...prestationRawValue,
      id: { value: prestationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PrestationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
    };
  }

  private convertPrestationRawValueToPrestation(
    rawPrestation: PrestationFormRawValue | NewPrestationFormRawValue,
  ): IPrestation | NewPrestation {
    return {
      ...rawPrestation,
      dateDebut: dayjs(rawPrestation.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawPrestation.dateFin, DATE_TIME_FORMAT),
    };
  }

  private convertPrestationToPrestationRawValue(
    prestation: IPrestation | (Partial<NewPrestation> & PrestationFormDefaults),
  ): PrestationFormRawValue | PartialWithRequiredKeyOf<NewPrestationFormRawValue> {
    return {
      ...prestation,
      dateDebut: prestation.dateDebut ? prestation.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: prestation.dateFin ? prestation.dateFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
