import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';

export interface IPrestation {
  id: string;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  demande?: Pick<IDemande, 'id'> | null;
  partenaire?: Pick<IPartenaire, 'id' | 'libelle'> | null;
}

export type NewPrestation = Omit<IPrestation, 'id'> & { id: null };
