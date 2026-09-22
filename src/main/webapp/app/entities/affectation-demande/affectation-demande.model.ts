import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';

export interface IAffectationDemande {
  id: string;
  dateAffectation?: dayjs.Dayjs | null;
  demande?: Pick<IDemande, 'id'> | null;
  partenaire?: Pick<IPartenaire, 'id' | 'libelle'> | null;
}

export type NewAffectationDemande = Omit<IAffectationDemande, 'id'> & { id: null };
