import { IProfil, NewProfil } from './profil.model';

export const sampleWithRequiredData: IProfil = {
  id: 'a102e05c-c665-4913-9bea-de7b10401ae1',
  code: 'pourvu que',
  libelle: "administration malgré d'entre",
};

export const sampleWithPartialData: IProfil = {
  id: '24fa135b-f9d3-4069-98a8-708fcec3989d',
  code: 'candide antique',
  libelle: 'du moment que',
};

export const sampleWithFullData: IProfil = {
  id: 'f05140fd-c0db-4460-9ede-30b48cebb6f9',
  code: 'imaginer',
  libelle: 'regretter forcer sitôt que',
  description: 'meuh',
};

export const sampleWithNewData: NewProfil = {
  code: 'coin-coin coac coac',
  libelle: 'ronron',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
