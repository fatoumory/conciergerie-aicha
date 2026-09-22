import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IQuotaService, NewQuotaService } from '../quota-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuotaService for edit and NewQuotaServiceFormGroupInput for create.
 */
type QuotaServiceFormGroupInput = IQuotaService | PartialWithRequiredKeyOf<NewQuotaService>;

type QuotaServiceFormDefaults = Pick<NewQuotaService, 'id'>;

type QuotaServiceFormGroupContent = {
  id: FormControl<IQuotaService['id'] | NewQuotaService['id']>;
  limite: FormControl<IQuotaService['limite']>;
  unite: FormControl<IQuotaService['unite']>;
  periode: FormControl<IQuotaService['periode']>;
  dateDebut: FormControl<IQuotaService['dateDebut']>;
  dateFin: FormControl<IQuotaService['dateFin']>;
  eligibiliteService: FormControl<IQuotaService['eligibiliteService']>;
};

export type QuotaServiceFormGroup = FormGroup<QuotaServiceFormGroupContent>;

@Service()
export class QuotaServiceFormService {
  createQuotaServiceFormGroup(quotaService?: QuotaServiceFormGroupInput): QuotaServiceFormGroup {
    const quotaServiceRawValue = {
      ...this.getFormDefaults(),
      ...(quotaService ?? { id: null }),
    };

    return new FormGroup<QuotaServiceFormGroupContent>({
      id: new FormControl(
        { value: quotaServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      limite: new FormControl(quotaServiceRawValue.limite, {
        validators: [Validators.required, Validators.min(0)],
      }),
      unite: new FormControl(quotaServiceRawValue.unite, {
        validators: [Validators.required],
      }),
      periode: new FormControl(quotaServiceRawValue.periode, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(quotaServiceRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(quotaServiceRawValue.dateFin),
      eligibiliteService: new FormControl(quotaServiceRawValue.eligibiliteService, {
        validators: [Validators.required],
      }),
    });
  }

  getQuotaService(form: QuotaServiceFormGroup): IQuotaService | NewQuotaService {
    return form.getRawValue();
  }

  resetForm(form: QuotaServiceFormGroup, quotaService: QuotaServiceFormGroupInput): void {
    const quotaServiceRawValue = { ...this.getFormDefaults(), ...quotaService };
    form.reset({
      ...quotaServiceRawValue,
      id: { value: quotaServiceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): QuotaServiceFormDefaults {
    return {
      id: null,
    };
  }
}
