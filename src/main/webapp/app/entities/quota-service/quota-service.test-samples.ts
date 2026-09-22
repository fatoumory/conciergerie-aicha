import dayjs from 'dayjs/esm';

import { IQuotaService, NewQuotaService } from './quota-service.model';

export const sampleWithRequiredData: IQuotaService = {
  id: '1b07366c-5065-4c1d-9dc6-65796cfa1078',
  limite: 5661.97,
  unite: 'COURSE',
  periode: 'MOIS',
  dateDebut: dayjs('2026-09-21'),
};

export const sampleWithPartialData: IQuotaService = {
  id: '28f9c5cc-8479-4bac-b9a4-687b271887f6',
  limite: 2325.32,
  unite: 'UNITE',
  periode: 'JOUR',
  dateDebut: dayjs('2026-09-22'),
  dateFin: dayjs('2026-09-22'),
};

export const sampleWithFullData: IQuotaService = {
  id: '7a0fa695-2ed3-4ccf-911e-4ac76ffb4b30',
  limite: 15562.8,
  unite: 'KG',
  periode: 'ANNEE',
  dateDebut: dayjs('2026-09-21'),
  dateFin: dayjs('2026-09-21'),
};

export const sampleWithNewData: NewQuotaService = {
  limite: 12640.39,
  unite: 'UNITE',
  periode: 'SEMAINE',
  dateDebut: dayjs('2026-09-22'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
