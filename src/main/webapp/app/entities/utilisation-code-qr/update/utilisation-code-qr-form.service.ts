import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IUtilisationCodeQr, NewUtilisationCodeQr } from '../utilisation-code-qr.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisationCodeQr for edit and NewUtilisationCodeQrFormGroupInput for create.
 */
type UtilisationCodeQrFormGroupInput = IUtilisationCodeQr | PartialWithRequiredKeyOf<NewUtilisationCodeQr>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUtilisationCodeQr | NewUtilisationCodeQr> = Omit<T, 'dateUtilisation'> & {
  dateUtilisation?: string | null;
};

type UtilisationCodeQrFormRawValue = FormValueOf<IUtilisationCodeQr>;

type NewUtilisationCodeQrFormRawValue = FormValueOf<NewUtilisationCodeQr>;

type UtilisationCodeQrFormDefaults = Pick<NewUtilisationCodeQr, 'id' | 'dateUtilisation'>;

type UtilisationCodeQrFormGroupContent = {
  id: FormControl<UtilisationCodeQrFormRawValue['id'] | NewUtilisationCodeQr['id']>;
  dateUtilisation: FormControl<UtilisationCodeQrFormRawValue['dateUtilisation']>;
  codeQrService: FormControl<UtilisationCodeQrFormRawValue['codeQrService']>;
  partenaire: FormControl<UtilisationCodeQrFormRawValue['partenaire']>;
};

export type UtilisationCodeQrFormGroup = FormGroup<UtilisationCodeQrFormGroupContent>;

@Service()
export class UtilisationCodeQrFormService {
  createUtilisationCodeQrFormGroup(utilisationCodeQr?: UtilisationCodeQrFormGroupInput): UtilisationCodeQrFormGroup {
    const utilisationCodeQrRawValue = this.convertUtilisationCodeQrToUtilisationCodeQrRawValue({
      ...this.getFormDefaults(),
      ...(utilisationCodeQr ?? { id: null }),
    });

    return new FormGroup<UtilisationCodeQrFormGroupContent>({
      id: new FormControl(
        { value: utilisationCodeQrRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateUtilisation: new FormControl(utilisationCodeQrRawValue.dateUtilisation, {
        validators: [Validators.required],
      }),
      codeQrService: new FormControl(utilisationCodeQrRawValue.codeQrService, {
        validators: [Validators.required],
      }),
      partenaire: new FormControl(utilisationCodeQrRawValue.partenaire, {
        validators: [Validators.required],
      }),
    });
  }

  getUtilisationCodeQr(form: UtilisationCodeQrFormGroup): IUtilisationCodeQr | NewUtilisationCodeQr {
    return this.convertUtilisationCodeQrRawValueToUtilisationCodeQr(form.getRawValue());
  }

  resetForm(form: UtilisationCodeQrFormGroup, utilisationCodeQr: UtilisationCodeQrFormGroupInput): void {
    const utilisationCodeQrRawValue = this.convertUtilisationCodeQrToUtilisationCodeQrRawValue({
      ...this.getFormDefaults(),
      ...utilisationCodeQr,
    });
    form.reset({
      ...utilisationCodeQrRawValue,
      id: { value: utilisationCodeQrRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): UtilisationCodeQrFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateUtilisation: currentTime,
    };
  }

  private convertUtilisationCodeQrRawValueToUtilisationCodeQr(
    rawUtilisationCodeQr: UtilisationCodeQrFormRawValue | NewUtilisationCodeQrFormRawValue,
  ): IUtilisationCodeQr | NewUtilisationCodeQr {
    return {
      ...rawUtilisationCodeQr,
      dateUtilisation: dayjs(rawUtilisationCodeQr.dateUtilisation, DATE_TIME_FORMAT),
    };
  }

  private convertUtilisationCodeQrToUtilisationCodeQrRawValue(
    utilisationCodeQr: IUtilisationCodeQr | (Partial<NewUtilisationCodeQr> & UtilisationCodeQrFormDefaults),
  ): UtilisationCodeQrFormRawValue | PartialWithRequiredKeyOf<NewUtilisationCodeQrFormRawValue> {
    return {
      ...utilisationCodeQr,
      dateUtilisation: utilisationCodeQr.dateUtilisation ? utilisationCodeQr.dateUtilisation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
