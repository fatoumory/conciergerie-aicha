import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';
import { StatutCodeQr } from 'app/entities/enumerations/statut-code-qr.model';

export interface ICodeQrService {
  id: string;
  code?: string | null;
  qrCode?: string | null;
  dateGeneration?: dayjs.Dayjs | null;
  dateExpiration?: dayjs.Dayjs | null;
  statut?: keyof typeof StatutCodeQr | null;
  demande?: Pick<IDemande, 'id'> | null;
}

export type NewCodeQrService = Omit<ICodeQrService, 'id'> & { id: null };
