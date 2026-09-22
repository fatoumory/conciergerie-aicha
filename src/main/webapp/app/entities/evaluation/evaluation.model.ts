import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';

export interface IEvaluation {
  id: string;
  note?: number | null;
  commentaire?: string | null;
  dateEvaluation?: dayjs.Dayjs | null;
  demande?: Pick<IDemande, 'id'> | null;
}

export type NewEvaluation = Omit<IEvaluation, 'id'> & { id: null };
