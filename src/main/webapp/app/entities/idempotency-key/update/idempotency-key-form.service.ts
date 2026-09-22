import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IIdempotencyKey, NewIdempotencyKey } from '../idempotency-key.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIdempotencyKey for edit and NewIdempotencyKeyFormGroupInput for create.
 */
type IdempotencyKeyFormGroupInput = IIdempotencyKey | PartialWithRequiredKeyOf<NewIdempotencyKey>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIdempotencyKey | NewIdempotencyKey> = Omit<T, 'dateCreation' | 'dateExpiration'> & {
  dateCreation?: string | null;
  dateExpiration?: string | null;
};

type IdempotencyKeyFormRawValue = FormValueOf<IIdempotencyKey>;

type NewIdempotencyKeyFormRawValue = FormValueOf<NewIdempotencyKey>;

type IdempotencyKeyFormDefaults = Pick<NewIdempotencyKey, 'id' | 'dateCreation' | 'dateExpiration'>;

type IdempotencyKeyFormGroupContent = {
  id: FormControl<IdempotencyKeyFormRawValue['id'] | NewIdempotencyKey['id']>;
  cle: FormControl<IdempotencyKeyFormRawValue['cle']>;
  typeOperation: FormControl<IdempotencyKeyFormRawValue['typeOperation']>;
  resourceId: FormControl<IdempotencyKeyFormRawValue['resourceId']>;
  dateCreation: FormControl<IdempotencyKeyFormRawValue['dateCreation']>;
  dateExpiration: FormControl<IdempotencyKeyFormRawValue['dateExpiration']>;
};

export type IdempotencyKeyFormGroup = FormGroup<IdempotencyKeyFormGroupContent>;

@Service()
export class IdempotencyKeyFormService {
  createIdempotencyKeyFormGroup(idempotencyKey?: IdempotencyKeyFormGroupInput): IdempotencyKeyFormGroup {
    const idempotencyKeyRawValue = this.convertIdempotencyKeyToIdempotencyKeyRawValue({
      ...this.getFormDefaults(),
      ...(idempotencyKey ?? { id: null }),
    });

    return new FormGroup<IdempotencyKeyFormGroupContent>({
      id: new FormControl(
        { value: idempotencyKeyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cle: new FormControl(idempotencyKeyRawValue.cle, {
        validators: [Validators.required],
      }),
      typeOperation: new FormControl(idempotencyKeyRawValue.typeOperation, {
        validators: [Validators.required],
      }),
      resourceId: new FormControl(idempotencyKeyRawValue.resourceId),
      dateCreation: new FormControl(idempotencyKeyRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      dateExpiration: new FormControl(idempotencyKeyRawValue.dateExpiration),
    });
  }

  getIdempotencyKey(form: IdempotencyKeyFormGroup): IIdempotencyKey | NewIdempotencyKey {
    return this.convertIdempotencyKeyRawValueToIdempotencyKey(form.getRawValue());
  }

  resetForm(form: IdempotencyKeyFormGroup, idempotencyKey: IdempotencyKeyFormGroupInput): void {
    const idempotencyKeyRawValue = this.convertIdempotencyKeyToIdempotencyKeyRawValue({ ...this.getFormDefaults(), ...idempotencyKey });
    form.reset({
      ...idempotencyKeyRawValue,
      id: { value: idempotencyKeyRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): IdempotencyKeyFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
      dateExpiration: currentTime,
    };
  }

  private convertIdempotencyKeyRawValueToIdempotencyKey(
    rawIdempotencyKey: IdempotencyKeyFormRawValue | NewIdempotencyKeyFormRawValue,
  ): IIdempotencyKey | NewIdempotencyKey {
    return {
      ...rawIdempotencyKey,
      dateCreation: dayjs(rawIdempotencyKey.dateCreation, DATE_TIME_FORMAT),
      dateExpiration: dayjs(rawIdempotencyKey.dateExpiration, DATE_TIME_FORMAT),
    };
  }

  private convertIdempotencyKeyToIdempotencyKeyRawValue(
    idempotencyKey: IIdempotencyKey | (Partial<NewIdempotencyKey> & IdempotencyKeyFormDefaults),
  ): IdempotencyKeyFormRawValue | PartialWithRequiredKeyOf<NewIdempotencyKeyFormRawValue> {
    return {
      ...idempotencyKey,
      dateCreation: idempotencyKey.dateCreation ? idempotencyKey.dateCreation.format(DATE_TIME_FORMAT) : undefined,
      dateExpiration: idempotencyKey.dateExpiration ? idempotencyKey.dateExpiration.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
