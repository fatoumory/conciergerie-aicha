import { ITypeDemande, NewTypeDemande } from './type-demande.model';

export const sampleWithRequiredData: ITypeDemande = {
  id: 'd997e1cc-4e78-4732-80a5-c17abac6edcc',
  code: 'lorsque chut figurer',
  libelle: 'admettre de façon à ce que',
};

export const sampleWithPartialData: ITypeDemande = {
  id: 'fdac02f0-7806-4e49-a5b5-a4c04cc5faf8',
  code: 'contredire',
  libelle: 'au-dehors',
};

export const sampleWithFullData: ITypeDemande = {
  id: 'fb948bf9-0acf-4a7c-a53e-fb972dc3c32d',
  code: 'rôder au prix de bien',
  libelle: 'population du Québec manifester',
};

export const sampleWithNewData: NewTypeDemande = {
  code: 'chef quand',
  libelle: 'porte-parole prévaloir',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
