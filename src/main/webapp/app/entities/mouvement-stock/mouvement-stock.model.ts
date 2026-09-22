import dayjs from 'dayjs/esm';

import { ICompteStock } from 'app/entities/compte-stock/compte-stock.model';
import { TypeMouvementStock } from 'app/entities/enumerations/type-mouvement-stock.model';

export interface IMouvementStock {
  id: string;
  type?: keyof typeof TypeMouvementStock | null;
  quantite?: number | null;
  dateTransaction?: dayjs.Dayjs | null;
  motif?: string | null;
  compteStock?: Pick<ICompteStock, 'id'> | null;
}

export type NewMouvementStock = Omit<IMouvementStock, 'id'> & { id: null };
