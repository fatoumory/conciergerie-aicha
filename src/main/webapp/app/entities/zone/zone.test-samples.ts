import { IZone, NewZone } from './zone.model';

export const sampleWithRequiredData: IZone = {
  id: 'f8a27a4a-4476-4413-86d3-a4f631fbb73a',
  code: 'immense juriste de façon à ce que',
  libelle: 'bien que',
};

export const sampleWithPartialData: IZone = {
  id: 'cfd6da93-90f7-40c7-a5f4-024005584acc',
  code: 'grrr informer délectable',
  libelle: 'plouf',
};

export const sampleWithFullData: IZone = {
  id: 'acfa5ca6-059f-4a43-8484-14cf324eb44e',
  code: 'tantôt chef à moins de',
  libelle: 'boum',
};

export const sampleWithNewData: NewZone = {
  code: 'ouin groin groin ensemble',
  libelle: 'en dehors de équipe de recherche aussitôt que',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
