import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IEvaluation, NewEvaluation } from '../evaluation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvaluation for edit and NewEvaluationFormGroupInput for create.
 */
type EvaluationFormGroupInput = IEvaluation | PartialWithRequiredKeyOf<NewEvaluation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvaluation | NewEvaluation> = Omit<T, 'dateEvaluation'> & {
  dateEvaluation?: string | null;
};

type EvaluationFormRawValue = FormValueOf<IEvaluation>;

type NewEvaluationFormRawValue = FormValueOf<NewEvaluation>;

type EvaluationFormDefaults = Pick<NewEvaluation, 'id' | 'dateEvaluation'>;

type EvaluationFormGroupContent = {
  id: FormControl<EvaluationFormRawValue['id'] | NewEvaluation['id']>;
  note: FormControl<EvaluationFormRawValue['note']>;
  commentaire: FormControl<EvaluationFormRawValue['commentaire']>;
  dateEvaluation: FormControl<EvaluationFormRawValue['dateEvaluation']>;
  demande: FormControl<EvaluationFormRawValue['demande']>;
};

export type EvaluationFormGroup = FormGroup<EvaluationFormGroupContent>;

@Service()
export class EvaluationFormService {
  createEvaluationFormGroup(evaluation?: EvaluationFormGroupInput): EvaluationFormGroup {
    const evaluationRawValue = this.convertEvaluationToEvaluationRawValue({
      ...this.getFormDefaults(),
      ...(evaluation ?? { id: null }),
    });

    return new FormGroup<EvaluationFormGroupContent>({
      id: new FormControl(
        { value: evaluationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(evaluationRawValue.note, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      commentaire: new FormControl(evaluationRawValue.commentaire),
      dateEvaluation: new FormControl(evaluationRawValue.dateEvaluation, {
        validators: [Validators.required],
      }),
      demande: new FormControl(evaluationRawValue.demande, {
        validators: [Validators.required],
      }),
    });
  }

  getEvaluation(form: EvaluationFormGroup): IEvaluation | NewEvaluation {
    return this.convertEvaluationRawValueToEvaluation(form.getRawValue());
  }

  resetForm(form: EvaluationFormGroup, evaluation: EvaluationFormGroupInput): void {
    const evaluationRawValue = this.convertEvaluationToEvaluationRawValue({ ...this.getFormDefaults(), ...evaluation });
    form.reset({
      ...evaluationRawValue,
      id: { value: evaluationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EvaluationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateEvaluation: currentTime,
    };
  }

  private convertEvaluationRawValueToEvaluation(
    rawEvaluation: EvaluationFormRawValue | NewEvaluationFormRawValue,
  ): IEvaluation | NewEvaluation {
    return {
      ...rawEvaluation,
      dateEvaluation: dayjs(rawEvaluation.dateEvaluation, DATE_TIME_FORMAT),
    };
  }

  private convertEvaluationToEvaluationRawValue(
    evaluation: IEvaluation | (Partial<NewEvaluation> & EvaluationFormDefaults),
  ): EvaluationFormRawValue | PartialWithRequiredKeyOf<NewEvaluationFormRawValue> {
    return {
      ...evaluation,
      dateEvaluation: evaluation.dateEvaluation ? evaluation.dateEvaluation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
