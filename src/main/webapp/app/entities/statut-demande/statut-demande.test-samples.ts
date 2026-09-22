import { IStatutDemande, NewStatutDemande } from './statut-demande.model';

export const sampleWithRequiredData: IStatutDemande = {
  id: '6715360a-46c9-43c1-835e-ef7b0bd60ace',
  code: 'loufoque',
  libelle: 'considérable vu que',
};

export const sampleWithPartialData: IStatutDemande = {
  id: 'd6495668-4a86-4049-aef7-b69250b7c307',
  code: 'autant badaboum chef de cuisine',
  libelle: 'dessous moyennant au-delà',
};

export const sampleWithFullData: IStatutDemande = {
  id: 'efb3f694-5f0e-4e30-a683-4c2115a5f0af',
  code: 'gens tellement',
  libelle: 'à la faveur de',
};

export const sampleWithNewData: NewStatutDemande = {
  code: 'corps enseignant',
  libelle: 'exprès passablement',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
