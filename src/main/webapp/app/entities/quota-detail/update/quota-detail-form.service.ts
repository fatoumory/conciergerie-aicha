import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IQuotaDetail, NewQuotaDetail } from '../quota-detail.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuotaDetail for edit and NewQuotaDetailFormGroupInput for create.
 */
type QuotaDetailFormGroupInput = IQuotaDetail | PartialWithRequiredKeyOf<NewQuotaDetail>;

type QuotaDetailFormDefaults = Pick<NewQuotaDetail, 'id'>;

type QuotaDetailFormGroupContent = {
  id: FormControl<IQuotaDetail['id'] | NewQuotaDetail['id']>;
  limite: FormControl<IQuotaDetail['limite']>;
  quotaService: FormControl<IQuotaDetail['quotaService']>;
  zone: FormControl<IQuotaDetail['zone']>;
};

export type QuotaDetailFormGroup = FormGroup<QuotaDetailFormGroupContent>;

@Service()
export class QuotaDetailFormService {
  createQuotaDetailFormGroup(quotaDetail?: QuotaDetailFormGroupInput): QuotaDetailFormGroup {
    const quotaDetailRawValue = {
      ...this.getFormDefaults(),
      ...(quotaDetail ?? { id: null }),
    };

    return new FormGroup<QuotaDetailFormGroupContent>({
      id: new FormControl(
        { value: quotaDetailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      limite: new FormControl(quotaDetailRawValue.limite, {
        validators: [Validators.required, Validators.min(0)],
      }),
      quotaService: new FormControl(quotaDetailRawValue.quotaService, {
        validators: [Validators.required],
      }),
      zone: new FormControl(quotaDetailRawValue.zone, {
        validators: [Validators.required],
      }),
    });
  }

  getQuotaDetail(form: QuotaDetailFormGroup): IQuotaDetail | NewQuotaDetail {
    return form.getRawValue();
  }

  resetForm(form: QuotaDetailFormGroup, quotaDetail: QuotaDetailFormGroupInput): void {
    const quotaDetailRawValue = { ...this.getFormDefaults(), ...quotaDetail };
    form.reset({
      ...quotaDetailRawValue,
      id: { value: quotaDetailRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuotaDetailFormDefaults {
    return {
      id: null,
    };
  }
}
