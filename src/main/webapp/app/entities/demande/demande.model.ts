import dayjs from 'dayjs/esm';

import { IClient } from 'app/entities/client/client.model';
import { ICodePromo } from 'app/entities/code-promo/code-promo.model';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { IStatutDemande } from 'app/entities/statut-demande/statut-demande.model';
import { ITypeDemande } from 'app/entities/type-demande/type-demande.model';

export interface IDemande {
  id: string;
  dateCreation?: dayjs.Dayjs | null;
  description?: string | null;
  client?: Pick<IClient, 'id' | 'numero'> | null;
  service?: Pick<IServiceConciergerie, 'id' | 'libelle'> | null;
  typeDemande?: Pick<ITypeDemande, 'id' | 'libelle'> | null;
  statut?: Pick<IStatutDemande, 'id' | 'libelle'> | null;
  codePromo?: Pick<ICodePromo, 'id' | 'code'> | null;
}

export type NewDemande = Omit<IDemande, 'id'> & { id: null };
