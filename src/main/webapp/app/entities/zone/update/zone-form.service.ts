import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IZone, NewZone } from '../zone.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IZone for edit and NewZoneFormGroupInput for create.
 */
type ZoneFormGroupInput = IZone | PartialWithRequiredKeyOf<NewZone>;

type ZoneFormDefaults = Pick<NewZone, 'id'>;

type ZoneFormGroupContent = {
  id: FormControl<IZone['id'] | NewZone['id']>;
  code: FormControl<IZone['code']>;
  libelle: FormControl<IZone['libelle']>;
};

export type ZoneFormGroup = FormGroup<ZoneFormGroupContent>;

@Service()
export class ZoneFormService {
  createZoneFormGroup(zone?: ZoneFormGroupInput): ZoneFormGroup {
    const zoneRawValue = {
      ...this.getFormDefaults(),
      ...(zone ?? { id: null }),
    };

    return new FormGroup<ZoneFormGroupContent>({
      id: new FormControl(
        { value: zoneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(zoneRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(zoneRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getZone(form: ZoneFormGroup): IZone | NewZone {
    return form.getRawValue();
  }

  resetForm(form: ZoneFormGroup, zone: ZoneFormGroupInput): void {
    const zoneRawValue = { ...this.getFormDefaults(), ...zone };
    form.reset({
      ...zoneRawValue,
      id: { value: zoneRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ZoneFormDefaults {
    return {
      id: null,
    };
  }
}
