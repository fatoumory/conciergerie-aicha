import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';
import { IStatutDemande } from 'app/entities/statut-demande/statut-demande.model';

export interface IHistoriqueStatutDemande {
  id: string;
  dateChangement?: dayjs.Dayjs | null;
  demande?: Pick<IDemande, 'id'> | null;
  statut?: Pick<IStatutDemande, 'id' | 'libelle'> | null;
}

export type NewHistoriqueStatutDemande = Omit<IHistoriqueStatutDemande, 'id'> & { id: null };
