import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISegmentClient, NewSegmentClient } from '../segment-client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISegmentClient for edit and NewSegmentClientFormGroupInput for create.
 */
type SegmentClientFormGroupInput = ISegmentClient | PartialWithRequiredKeyOf<NewSegmentClient>;

type SegmentClientFormDefaults = Pick<NewSegmentClient, 'id'>;

type SegmentClientFormGroupContent = {
  id: FormControl<ISegmentClient['id'] | NewSegmentClient['id']>;
  code: FormControl<ISegmentClient['code']>;
  libelle: FormControl<ISegmentClient['libelle']>;
};

export type SegmentClientFormGroup = FormGroup<SegmentClientFormGroupContent>;

@Service()
export class SegmentClientFormService {
  createSegmentClientFormGroup(segmentClient?: SegmentClientFormGroupInput): SegmentClientFormGroup {
    const segmentClientRawValue = {
      ...this.getFormDefaults(),
      ...(segmentClient ?? { id: null }),
    };

    return new FormGroup<SegmentClientFormGroupContent>({
      id: new FormControl(
        { value: segmentClientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(segmentClientRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(segmentClientRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getSegmentClient(form: SegmentClientFormGroup): ISegmentClient | NewSegmentClient {
    return form.getRawValue();
  }

  resetForm(form: SegmentClientFormGroup, segmentClient: SegmentClientFormGroupInput): void {
    const segmentClientRawValue = { ...this.getFormDefaults(), ...segmentClient };
    form.reset({
      ...segmentClientRawValue,
      id: { value: segmentClientRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SegmentClientFormDefaults {
    return {
      id: null,
    };
  }
}
