import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITypeClient, NewTypeClient } from '../type-client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeClient for edit and NewTypeClientFormGroupInput for create.
 */
type TypeClientFormGroupInput = ITypeClient | PartialWithRequiredKeyOf<NewTypeClient>;

type TypeClientFormDefaults = Pick<NewTypeClient, 'id'>;

type TypeClientFormGroupContent = {
  id: FormControl<ITypeClient['id'] | NewTypeClient['id']>;
  code: FormControl<ITypeClient['code']>;
  libelle: FormControl<ITypeClient['libelle']>;
};

export type TypeClientFormGroup = FormGroup<TypeClientFormGroupContent>;

@Service()
export class TypeClientFormService {
  createTypeClientFormGroup(typeClient?: TypeClientFormGroupInput): TypeClientFormGroup {
    const typeClientRawValue = {
      ...this.getFormDefaults(),
      ...(typeClient ?? { id: null }),
    };

    return new FormGroup<TypeClientFormGroupContent>({
      id: new FormControl(
        { value: typeClientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(typeClientRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(typeClientRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeClient(form: TypeClientFormGroup): ITypeClient | NewTypeClient {
    return form.getRawValue();
  }

  resetForm(form: TypeClientFormGroup, typeClient: TypeClientFormGroupInput): void {
    const typeClientRawValue = { ...this.getFormDefaults(), ...typeClient };
    form.reset({
      ...typeClientRawValue,
      id: { value: typeClientRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TypeClientFormDefaults {
    return {
      id: null,
    };
  }
}
