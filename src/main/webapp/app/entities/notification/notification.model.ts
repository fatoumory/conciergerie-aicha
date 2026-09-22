import dayjs from 'dayjs/esm';

import { IClient } from 'app/entities/client/client.model';
import { IDemande } from 'app/entities/demande/demande.model';

export interface INotification {
  id: string;
  titre?: string | null;
  message?: string | null;
  dateEnvoi?: dayjs.Dayjs | null;
  lu?: boolean | null;
  client?: Pick<IClient, 'id' | 'numero'> | null;
  demande?: Pick<IDemande, 'id'> | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
