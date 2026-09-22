import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICouverturePartenaire, NewCouverturePartenaire } from '../couverture-partenaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICouverturePartenaire for edit and NewCouverturePartenaireFormGroupInput for create.
 */
type CouverturePartenaireFormGroupInput = ICouverturePartenaire | PartialWithRequiredKeyOf<NewCouverturePartenaire>;

type CouverturePartenaireFormDefaults = Pick<NewCouverturePartenaire, 'id'>;

type CouverturePartenaireFormGroupContent = {
  id: FormControl<ICouverturePartenaire['id'] | NewCouverturePartenaire['id']>;
  dateDebut: FormControl<ICouverturePartenaire['dateDebut']>;
  dateFin: FormControl<ICouverturePartenaire['dateFin']>;
  partenaire: FormControl<ICouverturePartenaire['partenaire']>;
  service: FormControl<ICouverturePartenaire['service']>;
};

export type CouverturePartenaireFormGroup = FormGroup<CouverturePartenaireFormGroupContent>;

@Service()
export class CouverturePartenaireFormService {
  createCouverturePartenaireFormGroup(couverturePartenaire?: CouverturePartenaireFormGroupInput): CouverturePartenaireFormGroup {
    const couverturePartenaireRawValue = {
      ...this.getFormDefaults(),
      ...(couverturePartenaire ?? { id: null }),
    };

    return new FormGroup<CouverturePartenaireFormGroupContent>({
      id: new FormControl(
        { value: couverturePartenaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDebut: new FormControl(couverturePartenaireRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(couverturePartenaireRawValue.dateFin),
      partenaire: new FormControl(couverturePartenaireRawValue.partenaire, {
        validators: [Validators.required],
      }),
      service: new FormControl(couverturePartenaireRawValue.service, {
        validators: [Validators.required],
      }),
    });
  }

  getCouverturePartenaire(form: CouverturePartenaireFormGroup): ICouverturePartenaire | NewCouverturePartenaire {
    return form.getRawValue();
  }

  resetForm(form: CouverturePartenaireFormGroup, couverturePartenaire: CouverturePartenaireFormGroupInput): void {
    const couverturePartenaireRawValue = { ...this.getFormDefaults(), ...couverturePartenaire };
    form.reset({
      ...couverturePartenaireRawValue,
      id: { value: couverturePartenaireRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CouverturePartenaireFormDefaults {
    return {
      id: null,
    };
  }
}
