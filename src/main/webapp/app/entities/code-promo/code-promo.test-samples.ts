import dayjs from 'dayjs/esm';

import { ICodePromo, NewCodePromo } from './code-promo.model';

export const sampleWithRequiredData: ICodePromo = {
  id: '1334d620-63c3-4ada-b2a9-3aded4e72f9b',
  code: 'dorénavant',
  valeur: 29138.63,
  dateDebut: dayjs('2026-09-21'),
  dateFin: dayjs('2026-09-21'),
};

export const sampleWithPartialData: ICodePromo = {
  id: '37b7fa29-1157-4869-b465-c528e5f19e87',
  code: 'au cas où atchoum',
  valeur: 31633.75,
  dateDebut: dayjs('2026-09-22'),
  dateFin: dayjs('2026-09-21'),
};

export const sampleWithFullData: ICodePromo = {
  id: 'a1b06f43-00b8-4e70-85c2-ac05902f95f7',
  code: 'hé',
  valeur: 21784.19,
  dateDebut: dayjs('2026-09-22'),
  dateFin: dayjs('2026-09-22'),
};

export const sampleWithNewData: NewCodePromo = {
  code: 'dériver',
  valeur: 20196.54,
  dateDebut: dayjs('2026-09-22'),
  dateFin: dayjs('2026-09-21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
