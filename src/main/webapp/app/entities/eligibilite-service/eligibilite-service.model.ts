import dayjs from 'dayjs/esm';

import { ISegmentClient } from 'app/entities/segment-client/segment-client.model';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { ITypeClient } from 'app/entities/type-client/type-client.model';

export interface IEligibiliteService {
  id: string;
  autorise?: boolean | null;
  gratuit?: boolean | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  service?: Pick<IServiceConciergerie, 'id' | 'libelle'> | null;
  segmentClient?: Pick<ISegmentClient, 'id' | 'libelle'> | null;
  typeClient?: Pick<ITypeClient, 'id' | 'libelle'> | null;
}

export type NewEligibiliteService = Omit<IEligibiliteService, 'id'> & { id: null };
