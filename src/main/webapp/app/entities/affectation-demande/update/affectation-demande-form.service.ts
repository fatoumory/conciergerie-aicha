import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IAffectationDemande, NewAffectationDemande } from '../affectation-demande.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAffectationDemande for edit and NewAffectationDemandeFormGroupInput for create.
 */
type AffectationDemandeFormGroupInput = IAffectationDemande | PartialWithRequiredKeyOf<NewAffectationDemande>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAffectationDemande | NewAffectationDemande> = Omit<T, 'dateAffectation'> & {
  dateAffectation?: string | null;
};

type AffectationDemandeFormRawValue = FormValueOf<IAffectationDemande>;

type NewAffectationDemandeFormRawValue = FormValueOf<NewAffectationDemande>;

type AffectationDemandeFormDefaults = Pick<NewAffectationDemande, 'id' | 'dateAffectation'>;

type AffectationDemandeFormGroupContent = {
  id: FormControl<AffectationDemandeFormRawValue['id'] | NewAffectationDemande['id']>;
  dateAffectation: FormControl<AffectationDemandeFormRawValue['dateAffectation']>;
  demande: FormControl<AffectationDemandeFormRawValue['demande']>;
  partenaire: FormControl<AffectationDemandeFormRawValue['partenaire']>;
};

export type AffectationDemandeFormGroup = FormGroup<AffectationDemandeFormGroupContent>;

@Service()
export class AffectationDemandeFormService {
  createAffectationDemandeFormGroup(affectationDemande?: AffectationDemandeFormGroupInput): AffectationDemandeFormGroup {
    const affectationDemandeRawValue = this.convertAffectationDemandeToAffectationDemandeRawValue({
      ...this.getFormDefaults(),
      ...(affectationDemande ?? { id: null }),
    });

    return new FormGroup<AffectationDemandeFormGroupContent>({
      id: new FormControl(
        { value: affectationDemandeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateAffectation: new FormControl(affectationDemandeRawValue.dateAffectation, {
        validators: [Validators.required],
      }),
      demande: new FormControl(affectationDemandeRawValue.demande, {
        validators: [Validators.required],
      }),
      partenaire: new FormControl(affectationDemandeRawValue.partenaire, {
        validators: [Validators.required],
      }),
    });
  }

  getAffectationDemande(form: AffectationDemandeFormGroup): IAffectationDemande | NewAffectationDemande {
    return this.convertAffectationDemandeRawValueToAffectationDemande(form.getRawValue());
  }

  resetForm(form: AffectationDemandeFormGroup, affectationDemande: AffectationDemandeFormGroupInput): void {
    const affectationDemandeRawValue = this.convertAffectationDemandeToAffectationDemandeRawValue({
      ...this.getFormDefaults(),
      ...affectationDemande,
    });
    form.reset({
      ...affectationDemandeRawValue,
      id: { value: affectationDemandeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AffectationDemandeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateAffectation: currentTime,
    };
  }

  private convertAffectationDemandeRawValueToAffectationDemande(
    rawAffectationDemande: AffectationDemandeFormRawValue | NewAffectationDemandeFormRawValue,
  ): IAffectationDemande | NewAffectationDemande {
    return {
      ...rawAffectationDemande,
      dateAffectation: dayjs(rawAffectationDemande.dateAffectation, DATE_TIME_FORMAT),
    };
  }

  private convertAffectationDemandeToAffectationDemandeRawValue(
    affectationDemande: IAffectationDemande | (Partial<NewAffectationDemande> & AffectationDemandeFormDefaults),
  ): AffectationDemandeFormRawValue | PartialWithRequiredKeyOf<NewAffectationDemandeFormRawValue> {
    return {
      ...affectationDemande,
      dateAffectation: affectationDemande.dateAffectation ? affectationDemande.dateAffectation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
