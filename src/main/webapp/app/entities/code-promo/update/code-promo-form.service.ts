import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICodePromo, NewCodePromo } from '../code-promo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICodePromo for edit and NewCodePromoFormGroupInput for create.
 */
type CodePromoFormGroupInput = ICodePromo | PartialWithRequiredKeyOf<NewCodePromo>;

type CodePromoFormDefaults = Pick<NewCodePromo, 'id'>;

type CodePromoFormGroupContent = {
  id: FormControl<ICodePromo['id'] | NewCodePromo['id']>;
  code: FormControl<ICodePromo['code']>;
  valeur: FormControl<ICodePromo['valeur']>;
  dateDebut: FormControl<ICodePromo['dateDebut']>;
  dateFin: FormControl<ICodePromo['dateFin']>;
};

export type CodePromoFormGroup = FormGroup<CodePromoFormGroupContent>;

@Service()
export class CodePromoFormService {
  createCodePromoFormGroup(codePromo?: CodePromoFormGroupInput): CodePromoFormGroup {
    const codePromoRawValue = {
      ...this.getFormDefaults(),
      ...(codePromo ?? { id: null }),
    };

    return new FormGroup<CodePromoFormGroupContent>({
      id: new FormControl(
        { value: codePromoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(codePromoRawValue.code, {
        validators: [Validators.required],
      }),
      valeur: new FormControl(codePromoRawValue.valeur, {
        validators: [Validators.required, Validators.min(0)],
      }),
      dateDebut: new FormControl(codePromoRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(codePromoRawValue.dateFin, {
        validators: [Validators.required],
      }),
    });
  }

  getCodePromo(form: CodePromoFormGroup): ICodePromo | NewCodePromo {
    return form.getRawValue();
  }

  resetForm(form: CodePromoFormGroup, codePromo: CodePromoFormGroupInput): void {
    const codePromoRawValue = { ...this.getFormDefaults(), ...codePromo };
    form.reset({
      ...codePromoRawValue,
      id: { value: codePromoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CodePromoFormDefaults {
    return {
      id: null,
    };
  }
}
