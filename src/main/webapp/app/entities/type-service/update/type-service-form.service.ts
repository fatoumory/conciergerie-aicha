import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITypeService, NewTypeService } from '../type-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeService for edit and NewTypeServiceFormGroupInput for create.
 */
type TypeServiceFormGroupInput = ITypeService | PartialWithRequiredKeyOf<NewTypeService>;

type TypeServiceFormDefaults = Pick<NewTypeService, 'id'>;

type TypeServiceFormGroupContent = {
  id: FormControl<ITypeService['id'] | NewTypeService['id']>;
  code: FormControl<ITypeService['code']>;
  libelle: FormControl<ITypeService['libelle']>;
};

export type TypeServiceFormGroup = FormGroup<TypeServiceFormGroupContent>;

@Service()
export class TypeServiceFormService {
  createTypeServiceFormGroup(typeService?: TypeServiceFormGroupInput): TypeServiceFormGroup {
    const typeServiceRawValue = {
      ...this.getFormDefaults(),
      ...(typeService ?? { id: null }),
    };

    return new FormGroup<TypeServiceFormGroupContent>({
      id: new FormControl(
        { value: typeServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(typeServiceRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(typeServiceRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeService(form: TypeServiceFormGroup): ITypeService | NewTypeService {
    return form.getRawValue();
  }

  resetForm(form: TypeServiceFormGroup, typeService: TypeServiceFormGroupInput): void {
    const typeServiceRawValue = { ...this.getFormDefaults(), ...typeService };
    form.reset({
      ...typeServiceRawValue,
      id: { value: typeServiceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TypeServiceFormDefaults {
    return {
      id: null,
    };
  }
}
