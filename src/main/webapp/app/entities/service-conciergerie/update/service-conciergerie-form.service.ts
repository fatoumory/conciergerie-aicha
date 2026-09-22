import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IServiceConciergerie, NewServiceConciergerie } from '../service-conciergerie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceConciergerie for edit and NewServiceConciergerieFormGroupInput for create.
 */
type ServiceConciergerieFormGroupInput = IServiceConciergerie | PartialWithRequiredKeyOf<NewServiceConciergerie>;

type ServiceConciergerieFormDefaults = Pick<NewServiceConciergerie, 'id'>;

type ServiceConciergerieFormGroupContent = {
  id: FormControl<IServiceConciergerie['id'] | NewServiceConciergerie['id']>;
  code: FormControl<IServiceConciergerie['code']>;
  libelle: FormControl<IServiceConciergerie['libelle']>;
  description: FormControl<IServiceConciergerie['description']>;
  typeService: FormControl<IServiceConciergerie['typeService']>;
};

export type ServiceConciergerieFormGroup = FormGroup<ServiceConciergerieFormGroupContent>;

@Service()
export class ServiceConciergerieFormService {
  createServiceConciergerieFormGroup(serviceConciergerie?: ServiceConciergerieFormGroupInput): ServiceConciergerieFormGroup {
    const serviceConciergerieRawValue = {
      ...this.getFormDefaults(),
      ...(serviceConciergerie ?? { id: null }),
    };

    return new FormGroup<ServiceConciergerieFormGroupContent>({
      id: new FormControl(
        { value: serviceConciergerieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(serviceConciergerieRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(serviceConciergerieRawValue.libelle, {
        validators: [Validators.required],
      }),
      description: new FormControl(serviceConciergerieRawValue.description),
      typeService: new FormControl(serviceConciergerieRawValue.typeService, {
        validators: [Validators.required],
      }),
    });
  }

  getServiceConciergerie(form: ServiceConciergerieFormGroup): IServiceConciergerie | NewServiceConciergerie {
    return form.getRawValue();
  }

  resetForm(form: ServiceConciergerieFormGroup, serviceConciergerie: ServiceConciergerieFormGroupInput): void {
    const serviceConciergerieRawValue = { ...this.getFormDefaults(), ...serviceConciergerie };
    form.reset({
      ...serviceConciergerieRawValue,
      id: { value: serviceConciergerieRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ServiceConciergerieFormDefaults {
    return {
      id: null,
    };
  }
}
