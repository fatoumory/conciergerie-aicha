import dayjs from 'dayjs/esm';

import { IEvaluation, NewEvaluation } from './evaluation.model';

export const sampleWithRequiredData: IEvaluation = {
  id: 'eafa9606-554c-4b0e-817d-c07ed3806add',
  note: 3,
  dateEvaluation: dayjs('2026-09-21T20:32'),
};

export const sampleWithPartialData: IEvaluation = {
  id: '0edcbedd-d47f-4623-adad-e620897e0c39',
  note: 4,
  dateEvaluation: dayjs('2026-09-21T22:07'),
};

export const sampleWithFullData: IEvaluation = {
  id: 'fe79ce6d-bf9e-46b2-bd15-265795b0a552',
  note: 2,
  commentaire: '../fake-data/blob/hipster.txt',
  dateEvaluation: dayjs('2026-09-21T19:32'),
};

export const sampleWithNewData: NewEvaluation = {
  note: 3,
  dateEvaluation: dayjs('2026-09-22T07:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
