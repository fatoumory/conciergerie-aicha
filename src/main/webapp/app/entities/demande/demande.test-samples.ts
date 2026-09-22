import dayjs from 'dayjs/esm';

import { IDemande, NewDemande } from './demande.model';

export const sampleWithRequiredData: IDemande = {
  id: '647ad242-d185-4c07-970a-21224afd1cbc',
  dateCreation: dayjs('2026-09-22T03:34'),
};

export const sampleWithPartialData: IDemande = {
  id: '579f50f3-739f-4aa8-8809-6df236d843af',
  dateCreation: dayjs('2026-09-21T21:30'),
  description: 'même si barrer',
};

export const sampleWithFullData: IDemande = {
  id: '2b6780ff-c871-4121-b0ec-3b9c18acf871',
  dateCreation: dayjs('2026-09-21T18:38'),
  description: 'élaborer',
};

export const sampleWithNewData: NewDemande = {
  dateCreation: dayjs('2026-09-21T22:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
