import dayjs from 'dayjs/esm';

import { IEligibiliteService } from 'app/entities/eligibilite-service/eligibilite-service.model';
import { PeriodeQuota } from 'app/entities/enumerations/periode-quota.model';
import { UniteQuota } from 'app/entities/enumerations/unite-quota.model';

export interface IQuotaService {
  id: string;
  limite?: number | null;
  unite?: keyof typeof UniteQuota | null;
  periode?: keyof typeof PeriodeQuota | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  eligibiliteService?: Pick<IEligibiliteService, 'id'> | null;
}

export type NewQuotaService = Omit<IQuotaService, 'id'> & { id: null };
