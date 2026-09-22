import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IDemande, NewDemande } from '../demande.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDemande for edit and NewDemandeFormGroupInput for create.
 */
type DemandeFormGroupInput = IDemande | PartialWithRequiredKeyOf<NewDemande>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDemande | NewDemande> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type DemandeFormRawValue = FormValueOf<IDemande>;

type NewDemandeFormRawValue = FormValueOf<NewDemande>;

type DemandeFormDefaults = Pick<NewDemande, 'id' | 'dateCreation'>;

type DemandeFormGroupContent = {
  id: FormControl<DemandeFormRawValue['id'] | NewDemande['id']>;
  dateCreation: FormControl<DemandeFormRawValue['dateCreation']>;
  description: FormControl<DemandeFormRawValue['description']>;
  client: FormControl<DemandeFormRawValue['client']>;
  service: FormControl<DemandeFormRawValue['service']>;
  typeDemande: FormControl<DemandeFormRawValue['typeDemande']>;
  statut: FormControl<DemandeFormRawValue['statut']>;
  codePromo: FormControl<DemandeFormRawValue['codePromo']>;
};

export type DemandeFormGroup = FormGroup<DemandeFormGroupContent>;

@Service()
export class DemandeFormService {
  createDemandeFormGroup(demande?: DemandeFormGroupInput): DemandeFormGroup {
    const demandeRawValue = this.convertDemandeToDemandeRawValue({
      ...this.getFormDefaults(),
      ...(demande ?? { id: null }),
    });

    return new FormGroup<DemandeFormGroupContent>({
      id: new FormControl(
        { value: demandeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateCreation: new FormControl(demandeRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      description: new FormControl(demandeRawValue.description),
      client: new FormControl(demandeRawValue.client, {
        validators: [Validators.required],
      }),
      service: new FormControl(demandeRawValue.service, {
        validators: [Validators.required],
      }),
      typeDemande: new FormControl(demandeRawValue.typeDemande, {
        validators: [Validators.required],
      }),
      statut: new FormControl(demandeRawValue.statut, {
        validators: [Validators.required],
      }),
      codePromo: new FormControl(demandeRawValue.codePromo),
    });
  }

  getDemande(form: DemandeFormGroup): IDemande | NewDemande {
    return this.convertDemandeRawValueToDemande(form.getRawValue());
  }

  resetForm(form: DemandeFormGroup, demande: DemandeFormGroupInput): void {
    const demandeRawValue = this.convertDemandeToDemandeRawValue({ ...this.getFormDefaults(), ...demande });
    form.reset({
      ...demandeRawValue,
      id: { value: demandeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DemandeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
    };
  }

  private convertDemandeRawValueToDemande(rawDemande: DemandeFormRawValue | NewDemandeFormRawValue): IDemande | NewDemande {
    return {
      ...rawDemande,
      dateCreation: dayjs(rawDemande.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertDemandeToDemandeRawValue(
    demande: IDemande | (Partial<NewDemande> & DemandeFormDefaults),
  ): DemandeFormRawValue | PartialWithRequiredKeyOf<NewDemandeFormRawValue> {
    return {
      ...demande,
      dateCreation: demande.dateCreation ? demande.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
