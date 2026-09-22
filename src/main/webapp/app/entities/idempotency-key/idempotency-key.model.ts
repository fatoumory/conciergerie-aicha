import dayjs from 'dayjs/esm';

export interface IIdempotencyKey {
  id: string;
  cle?: string | null;
  typeOperation?: string | null;
  resourceId?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  dateExpiration?: dayjs.Dayjs | null;
}

export type NewIdempotencyKey = Omit<IIdempotencyKey, 'id'> & { id: null };
