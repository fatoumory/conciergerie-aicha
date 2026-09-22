import dayjs from 'dayjs/esm';

import { ICouverturePartenaire, NewCouverturePartenaire } from './couverture-partenaire.model';

export const sampleWithRequiredData: ICouverturePartenaire = {
  id: '4f4c242a-d95e-4919-a8c1-7e909fe133f7',
  dateDebut: dayjs('2026-09-21'),
};

export const sampleWithPartialData: ICouverturePartenaire = {
  id: '7349725a-cdd9-49c6-b84f-2d68a9a878e4',
  dateDebut: dayjs('2026-09-21'),
};

export const sampleWithFullData: ICouverturePartenaire = {
  id: '553d7e78-5064-4536-8417-9b131f4ae64d',
  dateDebut: dayjs('2026-09-21'),
  dateFin: dayjs('2026-09-22'),
};

export const sampleWithNewData: NewCouverturePartenaire = {
  dateDebut: dayjs('2026-09-22'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
