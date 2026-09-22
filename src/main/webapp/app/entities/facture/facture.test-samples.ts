import dayjs from 'dayjs/esm';

import { IFacture, NewFacture } from './facture.model';

export const sampleWithRequiredData: IFacture = {
  id: 'd07f7ed1-9ab4-47b3-8e9a-f45d10c4f9db',
  numero: 'ensuite',
  montant: 1935.4,
  dateGeneration: dayjs('2026-09-22T04:35'),
};

export const sampleWithPartialData: IFacture = {
  id: 'b24d25fb-e38a-4b70-9a04-e6f543bb679f',
  numero: 'dans la mesure où snif',
  montant: 8902.13,
  dateGeneration: dayjs('2026-09-21T22:52'),
};

export const sampleWithFullData: IFacture = {
  id: '2ed32671-c90e-43e6-9c50-a179590e930f',
  numero: 'vroum coac coac',
  montant: 22616.27,
  dateGeneration: dayjs('2026-09-22T06:35'),
};

export const sampleWithNewData: NewFacture = {
  numero: 'clac sitôt que altruiste',
  montant: 32225.07,
  dateGeneration: dayjs('2026-09-21T14:47'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
