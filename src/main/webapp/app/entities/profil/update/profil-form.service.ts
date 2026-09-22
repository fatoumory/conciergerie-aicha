import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProfil, NewProfil } from '../profil.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfil for edit and NewProfilFormGroupInput for create.
 */
type ProfilFormGroupInput = IProfil | PartialWithRequiredKeyOf<NewProfil>;

type ProfilFormDefaults = Pick<NewProfil, 'id' | 'utilisateurs' | 'roles'>;

type ProfilFormGroupContent = {
  id: FormControl<IProfil['id'] | NewProfil['id']>;
  code: FormControl<IProfil['code']>;
  libelle: FormControl<IProfil['libelle']>;
  description: FormControl<IProfil['description']>;
  utilisateurs: FormControl<IProfil['utilisateurs']>;
  roles: FormControl<IProfil['roles']>;
};

export type ProfilFormGroup = FormGroup<ProfilFormGroupContent>;

@Service()
export class ProfilFormService {
  createProfilFormGroup(profil?: ProfilFormGroupInput): ProfilFormGroup {
    const profilRawValue = {
      ...this.getFormDefaults(),
      ...(profil ?? { id: null }),
    };

    return new FormGroup<ProfilFormGroupContent>({
      id: new FormControl(
        { value: profilRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(profilRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(profilRawValue.libelle, {
        validators: [Validators.required],
      }),
      description: new FormControl(profilRawValue.description),
      utilisateurs: new FormControl(profilRawValue.utilisateurs ?? []),
      roles: new FormControl(profilRawValue.roles ?? []),
    });
  }

  getProfil(form: ProfilFormGroup): IProfil | NewProfil {
    return form.getRawValue();
  }

  resetForm(form: ProfilFormGroup, profil: ProfilFormGroupInput): void {
    const profilRawValue = { ...this.getFormDefaults(), ...profil };
    form.reset({
      ...profilRawValue,
      id: { value: profilRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProfilFormDefaults {
    return {
      id: null,
      utilisateurs: [],
      roles: [],
    };
  }
}
