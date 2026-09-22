import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';

export interface ICompteStock {
  id: string;
  solde?: number | null;
  service?: Pick<IServiceConciergerie, 'id' | 'libelle'> | null;
}

export type NewCompteStock = Omit<ICompteStock, 'id'> & { id: null };
