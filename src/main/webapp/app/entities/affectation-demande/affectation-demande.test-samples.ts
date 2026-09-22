import dayjs from 'dayjs/esm';

import { IAffectationDemande, NewAffectationDemande } from './affectation-demande.model';

export const sampleWithRequiredData: IAffectationDemande = {
  id: '8412e41f-1332-4cb8-a88f-cd2e5ffe1829',
  dateAffectation: dayjs('2026-09-22T00:00'),
};

export const sampleWithPartialData: IAffectationDemande = {
  id: '7d6f62d2-474f-489e-8143-b0467bd86b16',
  dateAffectation: dayjs('2026-09-21T15:52'),
};

export const sampleWithFullData: IAffectationDemande = {
  id: 'b2eb22ef-acad-4553-b99e-0e475ca76f2e',
  dateAffectation: dayjs('2026-09-22T09:00'),
};

export const sampleWithNewData: NewAffectationDemande = {
  dateAffectation: dayjs('2026-09-21T16:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
