import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClient, NewClient } from '../client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClient for edit and NewClientFormGroupInput for create.
 */
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;

type ClientFormDefaults = Pick<NewClient, 'id'>;

type ClientFormGroupContent = {
  id: FormControl<IClient['id'] | NewClient['id']>;
  numero: FormControl<IClient['numero']>;
  prenom: FormControl<IClient['prenom']>;
  nom: FormControl<IClient['nom']>;
  email: FormControl<IClient['email']>;
  user: FormControl<IClient['user']>;
  typeClient: FormControl<IClient['typeClient']>;
  segmentClient: FormControl<IClient['segmentClient']>;
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Service()
export class ClientFormService {
  createClientFormGroup(client?: ClientFormGroupInput): ClientFormGroup {
    const clientRawValue = {
      ...this.getFormDefaults(),
      ...(client ?? { id: null }),
    };

    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl(
        { value: clientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: new FormControl(clientRawValue.numero, {
        validators: [Validators.required],
      }),
      prenom: new FormControl(clientRawValue.prenom),
      nom: new FormControl(clientRawValue.nom),
      email: new FormControl(clientRawValue.email),
      user: new FormControl(clientRawValue.user),
      typeClient: new FormControl(clientRawValue.typeClient, {
        validators: [Validators.required],
      }),
      segmentClient: new FormControl(clientRawValue.segmentClient, {
        validators: [Validators.required],
      }),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return form.getRawValue();
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = { ...this.getFormDefaults(), ...client };
    form.reset({
      ...clientRawValue,
      id: { value: clientRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ClientFormDefaults {
    return {
      id: null,
    };
  }
}
