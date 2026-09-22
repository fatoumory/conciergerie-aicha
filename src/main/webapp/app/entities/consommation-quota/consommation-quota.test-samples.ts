import dayjs from 'dayjs/esm';

import { IConsommationQuota, NewConsommationQuota } from './consommation-quota.model';

export const sampleWithRequiredData: IConsommationQuota = {
  id: 'bf43ae6e-1304-4544-9c09-78d0345ef9ad',
  quantite: 2403.34,
  dateConsommation: dayjs('2026-09-21T16:13'),
};

export const sampleWithPartialData: IConsommationQuota = {
  id: 'dff1692c-e96a-48fe-a6bc-34b3e95d9078',
  quantite: 26154.28,
  dateConsommation: dayjs('2026-09-21T18:25'),
};

export const sampleWithFullData: IConsommationQuota = {
  id: 'f4dab81f-bb3e-4428-8a77-5a9e802737b6',
  quantite: 11875.66,
  dateConsommation: dayjs('2026-09-22T00:08'),
};

export const sampleWithNewData: NewConsommationQuota = {
  quantite: 5369.86,
  dateConsommation: dayjs('2026-09-22T01:13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
