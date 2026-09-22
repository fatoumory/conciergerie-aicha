import dayjs from 'dayjs/esm';

import { IPrestation, NewPrestation } from './prestation.model';

export const sampleWithRequiredData: IPrestation = {
  id: 'a307d848-8278-45ae-a8c5-3f7cdc3cc991',
};

export const sampleWithPartialData: IPrestation = {
  id: 'efa30862-d6b1-41d4-8887-a3b2ce33cb23',
};

export const sampleWithFullData: IPrestation = {
  id: '3fb47048-090f-4966-a28b-ea6cd76103a3',
  dateDebut: dayjs('2026-09-21T09:11'),
  dateFin: dayjs('2026-09-21T21:33'),
};

export const sampleWithNewData: NewPrestation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
