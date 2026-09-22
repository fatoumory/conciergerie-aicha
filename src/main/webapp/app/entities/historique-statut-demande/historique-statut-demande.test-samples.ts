import dayjs from 'dayjs/esm';

import { IHistoriqueStatutDemande, NewHistoriqueStatutDemande } from './historique-statut-demande.model';

export const sampleWithRequiredData: IHistoriqueStatutDemande = {
  id: '7f9facb9-80c4-41dc-82cf-6d0630194128',
  dateChangement: dayjs('2026-09-21T15:15'),
};

export const sampleWithPartialData: IHistoriqueStatutDemande = {
  id: '5a6d9db4-9e6e-478c-8863-0c9083db25d8',
  dateChangement: dayjs('2026-09-22T04:38'),
};

export const sampleWithFullData: IHistoriqueStatutDemande = {
  id: '1922e28a-8de0-48d1-82d9-3fcc595d2c5f',
  dateChangement: dayjs('2026-09-21T15:35'),
};

export const sampleWithNewData: NewHistoriqueStatutDemande = {
  dateChangement: dayjs('2026-09-22T03:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
