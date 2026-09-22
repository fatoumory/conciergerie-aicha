import dayjs from 'dayjs/esm';

import { IMouvementStock, NewMouvementStock } from './mouvement-stock.model';

export const sampleWithRequiredData: IMouvementStock = {
  id: '8d4561e6-9b2f-4746-8710-3959d42df31d',
  type: 'CREDIT',
  quantite: 28204.32,
  dateTransaction: dayjs('2026-09-21T13:06'),
};

export const sampleWithPartialData: IMouvementStock = {
  id: '5557f1bf-83ef-4be5-b2b8-e20064547521',
  type: 'DEBIT',
  quantite: 28213.14,
  dateTransaction: dayjs('2026-09-21T14:02'),
  motif: 'hôte magenta',
};

export const sampleWithFullData: IMouvementStock = {
  id: '1d260f1d-522e-427f-a452-e4c7731cbac8',
  type: 'DEBIT',
  quantite: 22086.74,
  dateTransaction: dayjs('2026-09-22T04:40'),
  motif: 'jeter croâ plic',
};

export const sampleWithNewData: NewMouvementStock = {
  type: 'CREDIT',
  quantite: 9745.25,
  dateTransaction: dayjs('2026-09-21T16:24'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
