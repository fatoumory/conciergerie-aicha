import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: '7d28d909-c61e-4284-a2e7-f5439e53da45',
  numero: 'toc-toc de manière à',
};

export const sampleWithPartialData: IClient = {
  id: '406d73e8-409e-479d-a07d-41574f6509ba',
  numero: 'à bas de à la merci',
  prenom: 'gémir que membre titulaire',
  nom: 'calme',
  email: 'Debora_Noel6@gmail.com',
};

export const sampleWithFullData: IClient = {
  id: 'ff15c7e3-2cf0-47c8-ac48-a89dc8e5f529',
  numero: 'a diplomate pauvre',
  prenom: 'areu areu commissionnaire diététiste',
  nom: 'tic-tac',
  email: 'Lucie_Roche@gmail.com',
};

export const sampleWithNewData: NewClient = {
  numero: 'tôt à la merci',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
