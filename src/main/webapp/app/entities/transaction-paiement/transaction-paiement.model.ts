import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';
import { ModePaiement } from 'app/entities/enumerations/mode-paiement.model';
import { StatutTransaction } from 'app/entities/enumerations/statut-transaction.model';

export interface ITransactionPaiement {
  id: string;
  montant?: number | null;
  modePaiement?: keyof typeof ModePaiement | null;
  statut?: keyof typeof StatutTransaction | null;
  referenceExterne?: string | null;
  dateTransaction?: dayjs.Dayjs | null;
  demande?: Pick<IDemande, 'id'> | null;
}

export type NewTransactionPaiement = Omit<ITransactionPaiement, 'id'> & { id: null };
