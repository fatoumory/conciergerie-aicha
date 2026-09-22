import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICompteStock, NewCompteStock } from '../compte-stock.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompteStock for edit and NewCompteStockFormGroupInput for create.
 */
type CompteStockFormGroupInput = ICompteStock | PartialWithRequiredKeyOf<NewCompteStock>;

type CompteStockFormDefaults = Pick<NewCompteStock, 'id'>;

type CompteStockFormGroupContent = {
  id: FormControl<ICompteStock['id'] | NewCompteStock['id']>;
  solde: FormControl<ICompteStock['solde']>;
  service: FormControl<ICompteStock['service']>;
};

export type CompteStockFormGroup = FormGroup<CompteStockFormGroupContent>;

@Service()
export class CompteStockFormService {
  createCompteStockFormGroup(compteStock?: CompteStockFormGroupInput): CompteStockFormGroup {
    const compteStockRawValue = {
      ...this.getFormDefaults(),
      ...(compteStock ?? { id: null }),
    };

    return new FormGroup<CompteStockFormGroupContent>({
      id: new FormControl(
        { value: compteStockRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      solde: new FormControl(compteStockRawValue.solde, {
        validators: [Validators.required],
      }),
      service: new FormControl(compteStockRawValue.service, {
        validators: [Validators.required],
      }),
    });
  }

  getCompteStock(form: CompteStockFormGroup): ICompteStock | NewCompteStock {
    return form.getRawValue();
  }

  resetForm(form: CompteStockFormGroup, compteStock: CompteStockFormGroupInput): void {
    const compteStockRawValue = { ...this.getFormDefaults(), ...compteStock };
    form.reset({
      ...compteStockRawValue,
      id: { value: compteStockRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CompteStockFormDefaults {
    return {
      id: null,
    };
  }
}
