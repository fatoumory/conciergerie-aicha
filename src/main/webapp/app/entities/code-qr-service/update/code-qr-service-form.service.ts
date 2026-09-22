import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ICodeQrService, NewCodeQrService } from '../code-qr-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICodeQrService for edit and NewCodeQrServiceFormGroupInput for create.
 */
type CodeQrServiceFormGroupInput = ICodeQrService | PartialWithRequiredKeyOf<NewCodeQrService>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICodeQrService | NewCodeQrService> = Omit<T, 'dateGeneration' | 'dateExpiration'> & {
  dateGeneration?: string | null;
  dateExpiration?: string | null;
};

type CodeQrServiceFormRawValue = FormValueOf<ICodeQrService>;

type NewCodeQrServiceFormRawValue = FormValueOf<NewCodeQrService>;

type CodeQrServiceFormDefaults = Pick<NewCodeQrService, 'id' | 'dateGeneration' | 'dateExpiration'>;

type CodeQrServiceFormGroupContent = {
  id: FormControl<CodeQrServiceFormRawValue['id'] | NewCodeQrService['id']>;
  code: FormControl<CodeQrServiceFormRawValue['code']>;
  qrCode: FormControl<CodeQrServiceFormRawValue['qrCode']>;
  dateGeneration: FormControl<CodeQrServiceFormRawValue['dateGeneration']>;
  dateExpiration: FormControl<CodeQrServiceFormRawValue['dateExpiration']>;
  statut: FormControl<CodeQrServiceFormRawValue['statut']>;
  demande: FormControl<CodeQrServiceFormRawValue['demande']>;
};

export type CodeQrServiceFormGroup = FormGroup<CodeQrServiceFormGroupContent>;

@Service()
export class CodeQrServiceFormService {
  createCodeQrServiceFormGroup(codeQrService?: CodeQrServiceFormGroupInput): CodeQrServiceFormGroup {
    const codeQrServiceRawValue = this.convertCodeQrServiceToCodeQrServiceRawValue({
      ...this.getFormDefaults(),
      ...(codeQrService ?? { id: null }),
    });

    return new FormGroup<CodeQrServiceFormGroupContent>({
      id: new FormControl(
        { value: codeQrServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(codeQrServiceRawValue.code, {
        validators: [Validators.required],
      }),
      qrCode: new FormControl(codeQrServiceRawValue.qrCode, {
        validators: [Validators.required],
      }),
      dateGeneration: new FormControl(codeQrServiceRawValue.dateGeneration, {
        validators: [Validators.required],
      }),
      dateExpiration: new FormControl(codeQrServiceRawValue.dateExpiration),
      statut: new FormControl(codeQrServiceRawValue.statut, {
        validators: [Validators.required],
      }),
      demande: new FormControl(codeQrServiceRawValue.demande, {
        validators: [Validators.required],
      }),
    });
  }

  getCodeQrService(form: CodeQrServiceFormGroup): ICodeQrService | NewCodeQrService {
    return this.convertCodeQrServiceRawValueToCodeQrService(form.getRawValue());
  }

  resetForm(form: CodeQrServiceFormGroup, codeQrService: CodeQrServiceFormGroupInput): void {
    const codeQrServiceRawValue = this.convertCodeQrServiceToCodeQrServiceRawValue({ ...this.getFormDefaults(), ...codeQrService });
    form.reset({
      ...codeQrServiceRawValue,
      id: { value: codeQrServiceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CodeQrServiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateGeneration: currentTime,
      dateExpiration: currentTime,
    };
  }

  private convertCodeQrServiceRawValueToCodeQrService(
    rawCodeQrService: CodeQrServiceFormRawValue | NewCodeQrServiceFormRawValue,
  ): ICodeQrService | NewCodeQrService {
    return {
      ...rawCodeQrService,
      dateGeneration: dayjs(rawCodeQrService.dateGeneration, DATE_TIME_FORMAT),
      dateExpiration: dayjs(rawCodeQrService.dateExpiration, DATE_TIME_FORMAT),
    };
  }

  private convertCodeQrServiceToCodeQrServiceRawValue(
    codeQrService: ICodeQrService | (Partial<NewCodeQrService> & CodeQrServiceFormDefaults),
  ): CodeQrServiceFormRawValue | PartialWithRequiredKeyOf<NewCodeQrServiceFormRawValue> {
    return {
      ...codeQrService,
      dateGeneration: codeQrService.dateGeneration ? codeQrService.dateGeneration.format(DATE_TIME_FORMAT) : undefined,
      dateExpiration: codeQrService.dateExpiration ? codeQrService.dateExpiration.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
