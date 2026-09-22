import dayjs from 'dayjs/esm';

export interface IJournalAudit {
  id: string;
  action?: string | null;
  typeObjet?: string | null;
  objetId?: string | null;
  correlationId?: string | null;
  dateAction?: dayjs.Dayjs | null;
}

export type NewJournalAudit = Omit<IJournalAudit, 'id'> & { id: null };
