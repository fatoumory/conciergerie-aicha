import dayjs from 'dayjs/esm';

import { IUtilisationCodeQr, NewUtilisationCodeQr } from './utilisation-code-qr.model';

export const sampleWithRequiredData: IUtilisationCodeQr = {
  id: '4b2e4c44-a90f-44cb-af69-fd51f88a304a',
  dateUtilisation: dayjs('2026-09-22T05:31'),
};

export const sampleWithPartialData: IUtilisationCodeQr = {
  id: 'bfa90349-49d1-4eaf-94c8-ddb58d1b6d8e',
  dateUtilisation: dayjs('2026-09-21T18:40'),
};

export const sampleWithFullData: IUtilisationCodeQr = {
  id: '8b0f1416-7566-4a6b-ac5c-1b694d90f940',
  dateUtilisation: dayjs('2026-09-21T20:47'),
};

export const sampleWithNewData: NewUtilisationCodeQr = {
  dateUtilisation: dayjs('2026-09-21T18:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
