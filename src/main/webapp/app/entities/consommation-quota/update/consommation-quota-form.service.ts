import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IConsommationQuota, NewConsommationQuota } from '../consommation-quota.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsommationQuota for edit and NewConsommationQuotaFormGroupInput for create.
 */
type ConsommationQuotaFormGroupInput = IConsommationQuota | PartialWithRequiredKeyOf<NewConsommationQuota>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConsommationQuota | NewConsommationQuota> = Omit<T, 'dateConsommation'> & {
  dateConsommation?: string | null;
};

type ConsommationQuotaFormRawValue = FormValueOf<IConsommationQuota>;

type NewConsommationQuotaFormRawValue = FormValueOf<NewConsommationQuota>;

type ConsommationQuotaFormDefaults = Pick<NewConsommationQuota, 'id' | 'dateConsommation'>;

type ConsommationQuotaFormGroupContent = {
  id: FormControl<ConsommationQuotaFormRawValue['id'] | NewConsommationQuota['id']>;
  quantite: FormControl<ConsommationQuotaFormRawValue['quantite']>;
  dateConsommation: FormControl<ConsommationQuotaFormRawValue['dateConsommation']>;
  client: FormControl<ConsommationQuotaFormRawValue['client']>;
  quotaService: FormControl<ConsommationQuotaFormRawValue['quotaService']>;
  quotaDetail: FormControl<ConsommationQuotaFormRawValue['quotaDetail']>;
};

export type ConsommationQuotaFormGroup = FormGroup<ConsommationQuotaFormGroupContent>;

@Service()
export class ConsommationQuotaFormService {
  createConsommationQuotaFormGroup(consommationQuota?: ConsommationQuotaFormGroupInput): ConsommationQuotaFormGroup {
    const consommationQuotaRawValue = this.convertConsommationQuotaToConsommationQuotaRawValue({
      ...this.getFormDefaults(),
      ...(consommationQuota ?? { id: null }),
    });

    return new FormGroup<ConsommationQuotaFormGroupContent>({
      id: new FormControl(
        { value: consommationQuotaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantite: new FormControl(consommationQuotaRawValue.quantite, {
        validators: [Validators.required, Validators.min(0)],
      }),
      dateConsommation: new FormControl(consommationQuotaRawValue.dateConsommation, {
        validators: [Validators.required],
      }),
      client: new FormControl(consommationQuotaRawValue.client, {
        validators: [Validators.required],
      }),
      quotaService: new FormControl(consommationQuotaRawValue.quotaService, {
        validators: [Validators.required],
      }),
      quotaDetail: new FormControl(consommationQuotaRawValue.quotaDetail),
    });
  }

  getConsommationQuota(form: ConsommationQuotaFormGroup): IConsommationQuota | NewConsommationQuota {
    return this.convertConsommationQuotaRawValueToConsommationQuota(form.getRawValue());
  }

  resetForm(form: ConsommationQuotaFormGroup, consommationQuota: ConsommationQuotaFormGroupInput): void {
    const consommationQuotaRawValue = this.convertConsommationQuotaToConsommationQuotaRawValue({
      ...this.getFormDefaults(),
      ...consommationQuota,
    });
    form.reset({
      ...consommationQuotaRawValue,
      id: { value: consommationQuotaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ConsommationQuotaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateConsommation: currentTime,
    };
  }

  private convertConsommationQuotaRawValueToConsommationQuota(
    rawConsommationQuota: ConsommationQuotaFormRawValue | NewConsommationQuotaFormRawValue,
  ): IConsommationQuota | NewConsommationQuota {
    return {
      ...rawConsommationQuota,
      dateConsommation: dayjs(rawConsommationQuota.dateConsommation, DATE_TIME_FORMAT),
    };
  }

  private convertConsommationQuotaToConsommationQuotaRawValue(
    consommationQuota: IConsommationQuota | (Partial<NewConsommationQuota> & ConsommationQuotaFormDefaults),
  ): ConsommationQuotaFormRawValue | PartialWithRequiredKeyOf<NewConsommationQuotaFormRawValue> {
    return {
      ...consommationQuota,
      dateConsommation: consommationQuota.dateConsommation ? consommationQuota.dateConsommation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
