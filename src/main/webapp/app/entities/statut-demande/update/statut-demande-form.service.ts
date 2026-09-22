import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStatutDemande, NewStatutDemande } from '../statut-demande.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatutDemande for edit and NewStatutDemandeFormGroupInput for create.
 */
type StatutDemandeFormGroupInput = IStatutDemande | PartialWithRequiredKeyOf<NewStatutDemande>;

type StatutDemandeFormDefaults = Pick<NewStatutDemande, 'id'>;

type StatutDemandeFormGroupContent = {
  id: FormControl<IStatutDemande['id'] | NewStatutDemande['id']>;
  code: FormControl<IStatutDemande['code']>;
  libelle: FormControl<IStatutDemande['libelle']>;
};

export type StatutDemandeFormGroup = FormGroup<StatutDemandeFormGroupContent>;

@Service()
export class StatutDemandeFormService {
  createStatutDemandeFormGroup(statutDemande?: StatutDemandeFormGroupInput): StatutDemandeFormGroup {
    const statutDemandeRawValue = {
      ...this.getFormDefaults(),
      ...(statutDemande ?? { id: null }),
    };

    return new FormGroup<StatutDemandeFormGroupContent>({
      id: new FormControl(
        { value: statutDemandeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(statutDemandeRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(statutDemandeRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getStatutDemande(form: StatutDemandeFormGroup): IStatutDemande | NewStatutDemande {
    return form.getRawValue();
  }

  resetForm(form: StatutDemandeFormGroup, statutDemande: StatutDemandeFormGroupInput): void {
    const statutDemandeRawValue = { ...this.getFormDefaults(), ...statutDemande };
    form.reset({
      ...statutDemandeRawValue,
      id: { value: statutDemandeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): StatutDemandeFormDefaults {
    return {
      id: null,
    };
  }
}
