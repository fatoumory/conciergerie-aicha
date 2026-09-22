import dayjs from 'dayjs/esm';

import { ICodeQrService, NewCodeQrService } from './code-qr-service.model';

export const sampleWithRequiredData: ICodeQrService = {
  id: 'e885101d-f67d-4018-9cfe-238583066a70',
  code: 'doucement',
  qrCode: '../fake-data/blob/hipster.txt',
  dateGeneration: dayjs('2026-09-22T03:30'),
  statut: 'EXPIRE',
};

export const sampleWithPartialData: ICodeQrService = {
  id: '020a4040-9b01-4a6c-999b-61748178a4bf',
  code: 'touriste',
  qrCode: '../fake-data/blob/hipster.txt',
  dateGeneration: dayjs('2026-09-22T00:22'),
  statut: 'VALIDE',
};

export const sampleWithFullData: ICodeQrService = {
  id: 'ce0183a9-6f81-45a4-ae8d-4c8aadf7403d',
  code: 'efficace parmi',
  qrCode: '../fake-data/blob/hipster.txt',
  dateGeneration: dayjs('2026-09-21T10:44'),
  dateExpiration: dayjs('2026-09-21T19:20'),
  statut: 'UTILISE',
};

export const sampleWithNewData: NewCodeQrService = {
  code: 'innombrable orange',
  qrCode: '../fake-data/blob/hipster.txt',
  dateGeneration: dayjs('2026-09-22T03:13'),
  statut: 'EXPIRE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
