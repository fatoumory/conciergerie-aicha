import dayjs from 'dayjs/esm';

export interface ICodePromo {
  id: string;
  code?: string | null;
  valeur?: number | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
}

export type NewCodePromo = Omit<ICodePromo, 'id'> & { id: null };
