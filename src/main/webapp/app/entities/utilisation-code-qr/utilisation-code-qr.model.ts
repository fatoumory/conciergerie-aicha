import dayjs from 'dayjs/esm';

import { ICodeQrService } from 'app/entities/code-qr-service/code-qr-service.model';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';

export interface IUtilisationCodeQr {
  id: string;
  dateUtilisation?: dayjs.Dayjs | null;
  codeQrService?: Pick<ICodeQrService, 'id' | 'code'> | null;
  partenaire?: Pick<IPartenaire, 'id' | 'libelle'> | null;
}

export type NewUtilisationCodeQr = Omit<IUtilisationCodeQr, 'id'> & { id: null };
