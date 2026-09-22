import dayjs from 'dayjs/esm';

import { IEligibiliteService, NewEligibiliteService } from './eligibilite-service.model';

export const sampleWithRequiredData: IEligibiliteService = {
  id: '44b6d125-f355-4670-95cc-18ca4f1830c1',
  autorise: true,
  gratuit: false,
  dateDebut: dayjs('2026-09-22'),
};

export const sampleWithPartialData: IEligibiliteService = {
  id: '3f2a4a16-07fd-4600-b017-7a3b7a4643df',
  autorise: false,
  gratuit: false,
  dateDebut: dayjs('2026-09-21'),
  dateFin: dayjs('2026-09-21'),
};

export const sampleWithFullData: IEligibiliteService = {
  id: '42d253a0-b746-4436-b3e6-62a167d231db',
  autorise: false,
  gratuit: false,
  dateDebut: dayjs('2026-09-21'),
  dateFin: dayjs('2026-09-22'),
};

export const sampleWithNewData: NewEligibiliteService = {
  autorise: true,
  gratuit: true,
  dateDebut: dayjs('2026-09-22'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
