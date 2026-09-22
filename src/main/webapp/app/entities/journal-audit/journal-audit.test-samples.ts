import dayjs from 'dayjs/esm';

import { IJournalAudit, NewJournalAudit } from './journal-audit.model';

export const sampleWithRequiredData: IJournalAudit = {
  id: '4c250321-b9e8-4c71-8da8-229a3c9f7ebf',
  action: 'proclamer distinguer',
  typeObjet: 'souper',
  correlationId: 'quasi',
  dateAction: dayjs('2026-09-22T08:04'),
};

export const sampleWithPartialData: IJournalAudit = {
  id: 'a82f2bab-f357-410c-8a5b-0d274b2d087b',
  action: 'au lieu de clac envers',
  typeObjet: 'communauté étudiante',
  correlationId: 'sans que',
  dateAction: dayjs('2026-09-21T09:40'),
};

export const sampleWithFullData: IJournalAudit = {
  id: 'fcbb4100-6393-4ded-8b04-1f2278f05634',
  action: 'fort',
  typeObjet: 'innombrable incalculable rectangulaire',
  objetId: '4cd74bbc-28b8-4d86-bb12-76c28a9940fa',
  correlationId: 'sitôt que lasser',
  dateAction: dayjs('2026-09-21T15:40'),
};

export const sampleWithNewData: NewJournalAudit = {
  action: "plaider à l'entour de au prix de",
  typeObjet: 'souple',
  correlationId: 'téléphoner subito divinement',
  dateAction: dayjs('2026-09-22T08:58'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
