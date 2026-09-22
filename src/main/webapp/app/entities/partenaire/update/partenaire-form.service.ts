import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartenaire, NewPartenaire } from '../partenaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartenaire for edit and NewPartenaireFormGroupInput for create.
 */
type PartenaireFormGroupInput = IPartenaire | PartialWithRequiredKeyOf<NewPartenaire>;

type PartenaireFormDefaults = Pick<NewPartenaire, 'id'>;

type PartenaireFormGroupContent = {
  id: FormControl<IPartenaire['id'] | NewPartenaire['id']>;
  code: FormControl<IPartenaire['code']>;
  libelle: FormControl<IPartenaire['libelle']>;
};

export type PartenaireFormGroup = FormGroup<PartenaireFormGroupContent>;

@Service()
export class PartenaireFormService {
  createPartenaireFormGroup(partenaire?: PartenaireFormGroupInput): PartenaireFormGroup {
    const partenaireRawValue = {
      ...this.getFormDefaults(),
      ...(partenaire ?? { id: null }),
    };

    return new FormGroup<PartenaireFormGroupContent>({
      id: new FormControl(
        { value: partenaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(partenaireRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(partenaireRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getPartenaire(form: PartenaireFormGroup): IPartenaire | NewPartenaire {
    return form.getRawValue();
  }

  resetForm(form: PartenaireFormGroup, partenaire: PartenaireFormGroupInput): void {
    const partenaireRawValue = { ...this.getFormDefaults(), ...partenaire };
    form.reset({
      ...partenaireRawValue,
      id: { value: partenaireRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartenaireFormDefaults {
    return {
      id: null,
    };
  }
}
