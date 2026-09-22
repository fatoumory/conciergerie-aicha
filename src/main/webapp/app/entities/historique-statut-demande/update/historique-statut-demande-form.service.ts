import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IHistoriqueStatutDemande, NewHistoriqueStatutDemande } from '../historique-statut-demande.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHistoriqueStatutDemande for edit and NewHistoriqueStatutDemandeFormGroupInput for create.
 */
type HistoriqueStatutDemandeFormGroupInput = IHistoriqueStatutDemande | PartialWithRequiredKeyOf<NewHistoriqueStatutDemande>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHistoriqueStatutDemande | NewHistoriqueStatutDemande> = Omit<T, 'dateChangement'> & {
  dateChangement?: string | null;
};

type HistoriqueStatutDemandeFormRawValue = FormValueOf<IHistoriqueStatutDemande>;

type NewHistoriqueStatutDemandeFormRawValue = FormValueOf<NewHistoriqueStatutDemande>;

type HistoriqueStatutDemandeFormDefaults = Pick<NewHistoriqueStatutDemande, 'id' | 'dateChangement'>;

type HistoriqueStatutDemandeFormGroupContent = {
  id: FormControl<HistoriqueStatutDemandeFormRawValue['id'] | NewHistoriqueStatutDemande['id']>;
  dateChangement: FormControl<HistoriqueStatutDemandeFormRawValue['dateChangement']>;
  demande: FormControl<HistoriqueStatutDemandeFormRawValue['demande']>;
  statut: FormControl<HistoriqueStatutDemandeFormRawValue['statut']>;
};

export type HistoriqueStatutDemandeFormGroup = FormGroup<HistoriqueStatutDemandeFormGroupContent>;

@Service()
export class HistoriqueStatutDemandeFormService {
  createHistoriqueStatutDemandeFormGroup(
    historiqueStatutDemande?: HistoriqueStatutDemandeFormGroupInput,
  ): HistoriqueStatutDemandeFormGroup {
    const historiqueStatutDemandeRawValue = this.convertHistoriqueStatutDemandeToHistoriqueStatutDemandeRawValue({
      ...this.getFormDefaults(),
      ...(historiqueStatutDemande ?? { id: null }),
    });

    return new FormGroup<HistoriqueStatutDemandeFormGroupContent>({
      id: new FormControl(
        { value: historiqueStatutDemandeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateChangement: new FormControl(historiqueStatutDemandeRawValue.dateChangement, {
        validators: [Validators.required],
      }),
      demande: new FormControl(historiqueStatutDemandeRawValue.demande, {
        validators: [Validators.required],
      }),
      statut: new FormControl(historiqueStatutDemandeRawValue.statut, {
        validators: [Validators.required],
      }),
    });
  }

  getHistoriqueStatutDemande(form: HistoriqueStatutDemandeFormGroup): IHistoriqueStatutDemande | NewHistoriqueStatutDemande {
    return this.convertHistoriqueStatutDemandeRawValueToHistoriqueStatutDemande(form.getRawValue());
  }

  resetForm(form: HistoriqueStatutDemandeFormGroup, historiqueStatutDemande: HistoriqueStatutDemandeFormGroupInput): void {
    const historiqueStatutDemandeRawValue = this.convertHistoriqueStatutDemandeToHistoriqueStatutDemandeRawValue({
      ...this.getFormDefaults(),
      ...historiqueStatutDemande,
    });
    form.reset({
      ...historiqueStatutDemandeRawValue,
      id: { value: historiqueStatutDemandeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): HistoriqueStatutDemandeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateChangement: currentTime,
    };
  }

  private convertHistoriqueStatutDemandeRawValueToHistoriqueStatutDemande(
    rawHistoriqueStatutDemande: HistoriqueStatutDemandeFormRawValue | NewHistoriqueStatutDemandeFormRawValue,
  ): IHistoriqueStatutDemande | NewHistoriqueStatutDemande {
    return {
      ...rawHistoriqueStatutDemande,
      dateChangement: dayjs(rawHistoriqueStatutDemande.dateChangement, DATE_TIME_FORMAT),
    };
  }

  private convertHistoriqueStatutDemandeToHistoriqueStatutDemandeRawValue(
    historiqueStatutDemande: IHistoriqueStatutDemande | (Partial<NewHistoriqueStatutDemande> & HistoriqueStatutDemandeFormDefaults),
  ): HistoriqueStatutDemandeFormRawValue | PartialWithRequiredKeyOf<NewHistoriqueStatutDemandeFormRawValue> {
    return {
      ...historiqueStatutDemande,
      dateChangement: historiqueStatutDemande.dateChangement ? historiqueStatutDemande.dateChangement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
