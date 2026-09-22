import dayjs from 'dayjs/esm';

import { ITransactionPaiement, NewTransactionPaiement } from './transaction-paiement.model';

export const sampleWithRequiredData: ITransactionPaiement = {
  id: '061e944d-4132-4099-8780-1805e3fe4b46',
  montant: 2517.82,
  modePaiement: 'ORANGE_MONEY',
  statut: 'ANNULE',
  dateTransaction: dayjs('2026-09-22T01:19'),
};

export const sampleWithPartialData: ITransactionPaiement = {
  id: 'a37e532b-db38-4e83-bafd-6c79e75837c7',
  montant: 21890.71,
  modePaiement: 'ORANGE_MONEY',
  statut: 'SUCCES',
  dateTransaction: dayjs('2026-09-22T05:24'),
};

export const sampleWithFullData: ITransactionPaiement = {
  id: '4dcf7dbc-363b-46bd-a785-aecd5d2171f7',
  montant: 16005.11,
  modePaiement: 'ORANGE_MONEY',
  statut: 'ANNULE',
  referenceExterne: 'large que',
  dateTransaction: dayjs('2026-09-22T01:00'),
};

export const sampleWithNewData: NewTransactionPaiement = {
  montant: 138.05,
  modePaiement: 'ORANGE_MONEY',
  statut: 'SUCCES',
  dateTransaction: dayjs('2026-09-21T18:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
