import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IJournalAudit, NewJournalAudit } from '../journal-audit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJournalAudit for edit and NewJournalAuditFormGroupInput for create.
 */
type JournalAuditFormGroupInput = IJournalAudit | PartialWithRequiredKeyOf<NewJournalAudit>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IJournalAudit | NewJournalAudit> = Omit<T, 'dateAction'> & {
  dateAction?: string | null;
};

type JournalAuditFormRawValue = FormValueOf<IJournalAudit>;

type NewJournalAuditFormRawValue = FormValueOf<NewJournalAudit>;

type JournalAuditFormDefaults = Pick<NewJournalAudit, 'id' | 'dateAction'>;

type JournalAuditFormGroupContent = {
  id: FormControl<JournalAuditFormRawValue['id'] | NewJournalAudit['id']>;
  action: FormControl<JournalAuditFormRawValue['action']>;
  typeObjet: FormControl<JournalAuditFormRawValue['typeObjet']>;
  objetId: FormControl<JournalAuditFormRawValue['objetId']>;
  correlationId: FormControl<JournalAuditFormRawValue['correlationId']>;
  dateAction: FormControl<JournalAuditFormRawValue['dateAction']>;
};

export type JournalAuditFormGroup = FormGroup<JournalAuditFormGroupContent>;

@Service()
export class JournalAuditFormService {
  createJournalAuditFormGroup(journalAudit?: JournalAuditFormGroupInput): JournalAuditFormGroup {
    const journalAuditRawValue = this.convertJournalAuditToJournalAuditRawValue({
      ...this.getFormDefaults(),
      ...(journalAudit ?? { id: null }),
    });

    return new FormGroup<JournalAuditFormGroupContent>({
      id: new FormControl(
        { value: journalAuditRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      action: new FormControl(journalAuditRawValue.action, {
        validators: [Validators.required],
      }),
      typeObjet: new FormControl(journalAuditRawValue.typeObjet, {
        validators: [Validators.required],
      }),
      objetId: new FormControl(journalAuditRawValue.objetId),
      correlationId: new FormControl(journalAuditRawValue.correlationId, {
        validators: [Validators.required],
      }),
      dateAction: new FormControl(journalAuditRawValue.dateAction, {
        validators: [Validators.required],
      }),
    });
  }

  getJournalAudit(form: JournalAuditFormGroup): IJournalAudit | NewJournalAudit {
    return this.convertJournalAuditRawValueToJournalAudit(form.getRawValue());
  }

  resetForm(form: JournalAuditFormGroup, journalAudit: JournalAuditFormGroupInput): void {
    const journalAuditRawValue = this.convertJournalAuditToJournalAuditRawValue({ ...this.getFormDefaults(), ...journalAudit });
    form.reset({
      ...journalAuditRawValue,
      id: { value: journalAuditRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): JournalAuditFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateAction: currentTime,
    };
  }

  private convertJournalAuditRawValueToJournalAudit(
    rawJournalAudit: JournalAuditFormRawValue | NewJournalAuditFormRawValue,
  ): IJournalAudit | NewJournalAudit {
    return {
      ...rawJournalAudit,
      dateAction: dayjs(rawJournalAudit.dateAction, DATE_TIME_FORMAT),
    };
  }

  private convertJournalAuditToJournalAuditRawValue(
    journalAudit: IJournalAudit | (Partial<NewJournalAudit> & JournalAuditFormDefaults),
  ): JournalAuditFormRawValue | PartialWithRequiredKeyOf<NewJournalAuditFormRawValue> {
    return {
      ...journalAudit,
      dateAction: journalAudit.dateAction ? journalAudit.dateAction.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
