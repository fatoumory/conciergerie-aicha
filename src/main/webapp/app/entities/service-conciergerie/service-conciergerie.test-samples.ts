import { IServiceConciergerie, NewServiceConciergerie } from './service-conciergerie.model';

export const sampleWithRequiredData: IServiceConciergerie = {
  id: '68d542bb-6a80-46e0-86cd-962685a73224',
  code: 'communauté étudiante avant de',
  libelle: 'super paf',
};

export const sampleWithPartialData: IServiceConciergerie = {
  id: '7ad37db3-66cf-4abd-8df5-c11817c431dd',
  code: 'lors de dès que',
  libelle: 'arrière ouch',
};

export const sampleWithFullData: IServiceConciergerie = {
  id: '7c50ea7e-ff58-4cb8-ac4c-e6afce9245a3',
  code: 'ensemble',
  libelle: 'moins débile',
  description: 'blablabla clac sur',
};

export const sampleWithNewData: NewServiceConciergerie = {
  code: 'après clientèle',
  libelle: 'procéder ferme',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
