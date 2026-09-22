import dayjs from 'dayjs/esm';

import { IIdempotencyKey, NewIdempotencyKey } from './idempotency-key.model';

export const sampleWithRequiredData: IIdempotencyKey = {
  id: '2c9cd097-e692-4a3a-a0f7-31e73a6b585c',
  cle: 'de manière à ce que broum en vérité',
  typeOperation: 'gêner marron un peu',
  dateCreation: dayjs('2026-09-21T12:00'),
};

export const sampleWithPartialData: IIdempotencyKey = {
  id: '8edb9a6f-5de5-4a4b-b386-4a44605a75b7',
  cle: 'tôt',
  typeOperation: "à l'insu de",
  resourceId: '23351cbc-e061-4fc3-a6af-4d596620723b',
  dateCreation: dayjs('2026-09-21T16:22'),
};

export const sampleWithFullData: IIdempotencyKey = {
  id: 'b77f0b87-35aa-4277-9797-5b3c4c7824e2',
  cle: 'habile',
  typeOperation: 'comme dorénavant',
  resourceId: '9d5979c7-2415-4152-9e80-06ca5fa0f26f',
  dateCreation: dayjs('2026-09-21T19:58'),
  dateExpiration: dayjs('2026-09-21T09:19'),
};

export const sampleWithNewData: NewIdempotencyKey = {
  cle: 'sous devant',
  typeOperation: 'bien que',
  dateCreation: dayjs('2026-09-22T09:01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
