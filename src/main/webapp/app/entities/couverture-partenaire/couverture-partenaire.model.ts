import dayjs from 'dayjs/esm';

import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';

export interface ICouverturePartenaire {
  id: string;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  partenaire?: Pick<IPartenaire, 'id' | 'libelle'> | null;
  service?: Pick<IServiceConciergerie, 'id' | 'libelle'> | null;
}

export type NewCouverturePartenaire = Omit<ICouverturePartenaire, 'id'> & { id: null };
