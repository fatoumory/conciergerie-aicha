import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';

export interface IFacture {
  id: string;
  numero?: string | null;
  montant?: number | null;
  dateGeneration?: dayjs.Dayjs | null;
  demande?: Pick<IDemande, 'id'> | null;
}

export type NewFacture = Omit<IFacture, 'id'> & { id: null };
