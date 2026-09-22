import { ITypeClient, NewTypeClient } from './type-client.model';

export const sampleWithRequiredData: ITypeClient = {
  id: 'fe48a557-cb32-4c7c-9e22-ff808119bb0b',
  code: 'à défaut de',
  libelle: 'du moment que jusque avant',
};

export const sampleWithPartialData: ITypeClient = {
  id: 'b05f927d-8e07-4dff-bd7d-3ddc23307af7',
  code: 'franco lunatique vanter',
  libelle: 'ouille athlète pousser',
};

export const sampleWithFullData: ITypeClient = {
  id: '35143787-b5c3-406e-a0b5-8da2f4e55aa2',
  code: 'drelin personnel professionnel',
  libelle: 'pour que ouf',
};

export const sampleWithNewData: NewTypeClient = {
  code: 'cuicui visiter environ',
  libelle: 'triathlète',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
