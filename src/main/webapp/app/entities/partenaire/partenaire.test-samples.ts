import { IPartenaire, NewPartenaire } from './partenaire.model';

export const sampleWithRequiredData: IPartenaire = {
  id: 'baac150e-6443-409a-a0da-cf5ed979fae2',
  code: 'lectorat',
  libelle: 'ci collègue',
};

export const sampleWithPartialData: IPartenaire = {
  id: '58eda75e-5beb-464f-8232-f3269125be53',
  code: 'équipe coupable',
  libelle: 'entre parmi perfectionner',
};

export const sampleWithFullData: IPartenaire = {
  id: 'b5e7c531-76e8-49c7-b3a9-cfe4b1a78781',
  code: 'membre de l’équipe sédentaire quoique',
  libelle: 'géométrique membre titulaire',
};

export const sampleWithNewData: NewPartenaire = {
  code: 'si au-devant',
  libelle: 'boum parlementaire parlementaire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
