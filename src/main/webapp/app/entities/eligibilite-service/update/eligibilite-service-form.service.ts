import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEligibiliteService, NewEligibiliteService } from '../eligibilite-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEligibiliteService for edit and NewEligibiliteServiceFormGroupInput for create.
 */
type EligibiliteServiceFormGroupInput = IEligibiliteService | PartialWithRequiredKeyOf<NewEligibiliteService>;

type EligibiliteServiceFormDefaults = Pick<NewEligibiliteService, 'id' | 'autorise' | 'gratuit'>;

type EligibiliteServiceFormGroupContent = {
  id: FormControl<IEligibiliteService['id'] | NewEligibiliteService['id']>;
  autorise: FormControl<IEligibiliteService['autorise']>;
  gratuit: FormControl<IEligibiliteService['gratuit']>;
  dateDebut: FormControl<IEligibiliteService['dateDebut']>;
  dateFin: FormControl<IEligibiliteService['dateFin']>;
  service: FormControl<IEligibiliteService['service']>;
  segmentClient: FormControl<IEligibiliteService['segmentClient']>;
  typeClient: FormControl<IEligibiliteService['typeClient']>;
};

export type EligibiliteServiceFormGroup = FormGroup<EligibiliteServiceFormGroupContent>;

@Service()
export class EligibiliteServiceFormService {
  createEligibiliteServiceFormGroup(eligibiliteService?: EligibiliteServiceFormGroupInput): EligibiliteServiceFormGroup {
    const eligibiliteServiceRawValue = {
      ...this.getFormDefaults(),
      ...(eligibiliteService ?? { id: null }),
    };

    return new FormGroup<EligibiliteServiceFormGroupContent>({
      id: new FormControl(
        { value: eligibiliteServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      autorise: new FormControl(eligibiliteServiceRawValue.autorise, {
        validators: [Validators.required],
      }),
      gratuit: new FormControl(eligibiliteServiceRawValue.gratuit, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(eligibiliteServiceRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(eligibiliteServiceRawValue.dateFin),
      service: new FormControl(eligibiliteServiceRawValue.service, {
        validators: [Validators.required],
      }),
      segmentClient: new FormControl(eligibiliteServiceRawValue.segmentClient, {
        validators: [Validators.required],
      }),
      typeClient: new FormControl(eligibiliteServiceRawValue.typeClient, {
        validators: [Validators.required],
      }),
    });
  }

  getEligibiliteService(form: EligibiliteServiceFormGroup): IEligibiliteService | NewEligibiliteService {
    return form.getRawValue();
  }

  resetForm(form: EligibiliteServiceFormGroup, eligibiliteService: EligibiliteServiceFormGroupInput): void {
    const eligibiliteServiceRawValue = { ...this.getFormDefaults(), ...eligibiliteService };
    form.reset({
      ...eligibiliteServiceRawValue,
      id: { value: eligibiliteServiceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EligibiliteServiceFormDefaults {
    return {
      id: null,
      autorise: false,
      gratuit: false,
    };
  }
}
