import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITypeDemande, NewTypeDemande } from '../type-demande.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeDemande for edit and NewTypeDemandeFormGroupInput for create.
 */
type TypeDemandeFormGroupInput = ITypeDemande | PartialWithRequiredKeyOf<NewTypeDemande>;

type TypeDemandeFormDefaults = Pick<NewTypeDemande, 'id'>;

type TypeDemandeFormGroupContent = {
  id: FormControl<ITypeDemande['id'] | NewTypeDemande['id']>;
  code: FormControl<ITypeDemande['code']>;
  libelle: FormControl<ITypeDemande['libelle']>;
};

export type TypeDemandeFormGroup = FormGroup<TypeDemandeFormGroupContent>;

@Service()
export class TypeDemandeFormService {
  createTypeDemandeFormGroup(typeDemande?: TypeDemandeFormGroupInput): TypeDemandeFormGroup {
    const typeDemandeRawValue = {
      ...this.getFormDefaults(),
      ...(typeDemande ?? { id: null }),
    };

    return new FormGroup<TypeDemandeFormGroupContent>({
      id: new FormControl(
        { value: typeDemandeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(typeDemandeRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(typeDemandeRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeDemande(form: TypeDemandeFormGroup): ITypeDemande | NewTypeDemande {
    return form.getRawValue();
  }

  resetForm(form: TypeDemandeFormGroup, typeDemande: TypeDemandeFormGroupInput): void {
    const typeDemandeRawValue = { ...this.getFormDefaults(), ...typeDemande };
    form.reset({
      ...typeDemandeRawValue,
      id: { value: typeDemandeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TypeDemandeFormDefaults {
    return {
      id: null,
    };
  }
}
