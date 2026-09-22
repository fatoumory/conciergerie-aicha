import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPartenaireZone, NewPartenaireZone } from '../partenaire-zone.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPartenaireZone for edit and NewPartenaireZoneFormGroupInput for create.
 */
type PartenaireZoneFormGroupInput = IPartenaireZone | PartialWithRequiredKeyOf<NewPartenaireZone>;

type PartenaireZoneFormDefaults = Pick<NewPartenaireZone, 'id'>;

type PartenaireZoneFormGroupContent = {
  id: FormControl<IPartenaireZone['id'] | NewPartenaireZone['id']>;
  partenaire: FormControl<IPartenaireZone['partenaire']>;
  zone: FormControl<IPartenaireZone['zone']>;
};

export type PartenaireZoneFormGroup = FormGroup<PartenaireZoneFormGroupContent>;

@Service()
export class PartenaireZoneFormService {
  createPartenaireZoneFormGroup(partenaireZone?: PartenaireZoneFormGroupInput): PartenaireZoneFormGroup {
    const partenaireZoneRawValue = {
      ...this.getFormDefaults(),
      ...(partenaireZone ?? { id: null }),
    };

    return new FormGroup<PartenaireZoneFormGroupContent>({
      id: new FormControl(
        { value: partenaireZoneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      partenaire: new FormControl(partenaireZoneRawValue.partenaire, {
        validators: [Validators.required],
      }),
      zone: new FormControl(partenaireZoneRawValue.zone, {
        validators: [Validators.required],
      }),
    });
  }

  getPartenaireZone(form: PartenaireZoneFormGroup): IPartenaireZone | NewPartenaireZone {
    return form.getRawValue();
  }

  resetForm(form: PartenaireZoneFormGroup, partenaireZone: PartenaireZoneFormGroupInput): void {
    const partenaireZoneRawValue = { ...this.getFormDefaults(), ...partenaireZone };
    form.reset({
      ...partenaireZoneRawValue,
      id: { value: partenaireZoneRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PartenaireZoneFormDefaults {
    return {
      id: null,
    };
  }
}
