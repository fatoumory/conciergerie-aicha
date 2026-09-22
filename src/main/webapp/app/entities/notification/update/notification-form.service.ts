import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { INotification, NewNotification } from '../notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotification for edit and NewNotificationFormGroupInput for create.
 */
type NotificationFormGroupInput = INotification | PartialWithRequiredKeyOf<NewNotification>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotification | NewNotification> = Omit<T, 'dateEnvoi'> & {
  dateEnvoi?: string | null;
};

type NotificationFormRawValue = FormValueOf<INotification>;

type NewNotificationFormRawValue = FormValueOf<NewNotification>;

type NotificationFormDefaults = Pick<NewNotification, 'id' | 'dateEnvoi' | 'lu'>;

type NotificationFormGroupContent = {
  id: FormControl<NotificationFormRawValue['id'] | NewNotification['id']>;
  titre: FormControl<NotificationFormRawValue['titre']>;
  message: FormControl<NotificationFormRawValue['message']>;
  dateEnvoi: FormControl<NotificationFormRawValue['dateEnvoi']>;
  lu: FormControl<NotificationFormRawValue['lu']>;
  client: FormControl<NotificationFormRawValue['client']>;
  demande: FormControl<NotificationFormRawValue['demande']>;
};

export type NotificationFormGroup = FormGroup<NotificationFormGroupContent>;

@Service()
export class NotificationFormService {
  createNotificationFormGroup(notification?: NotificationFormGroupInput): NotificationFormGroup {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({
      ...this.getFormDefaults(),
      ...(notification ?? { id: null }),
    });

    return new FormGroup<NotificationFormGroupContent>({
      id: new FormControl(
        { value: notificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titre: new FormControl(notificationRawValue.titre, {
        validators: [Validators.required],
      }),
      message: new FormControl(notificationRawValue.message, {
        validators: [Validators.required],
      }),
      dateEnvoi: new FormControl(notificationRawValue.dateEnvoi, {
        validators: [Validators.required],
      }),
      lu: new FormControl(notificationRawValue.lu, {
        validators: [Validators.required],
      }),
      client: new FormControl(notificationRawValue.client, {
        validators: [Validators.required],
      }),
      demande: new FormControl(notificationRawValue.demande),
    });
  }

  getNotification(form: NotificationFormGroup): INotification | NewNotification {
    return this.convertNotificationRawValueToNotification(form.getRawValue());
  }

  resetForm(form: NotificationFormGroup, notification: NotificationFormGroupInput): void {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({ ...this.getFormDefaults(), ...notification });
    form.reset({
      ...notificationRawValue,
      id: { value: notificationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): NotificationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateEnvoi: currentTime,
      lu: false,
    };
  }

  private convertNotificationRawValueToNotification(
    rawNotification: NotificationFormRawValue | NewNotificationFormRawValue,
  ): INotification | NewNotification {
    return {
      ...rawNotification,
      dateEnvoi: dayjs(rawNotification.dateEnvoi, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationToNotificationRawValue(
    notification: INotification | (Partial<NewNotification> & NotificationFormDefaults),
  ): NotificationFormRawValue | PartialWithRequiredKeyOf<NewNotificationFormRawValue> {
    return {
      ...notification,
      dateEnvoi: notification.dateEnvoi ? notification.dateEnvoi.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
