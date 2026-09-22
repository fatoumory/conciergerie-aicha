import dayjs from 'dayjs/esm';

import { IClient } from 'app/entities/client/client.model';
import { IQuotaDetail } from 'app/entities/quota-detail/quota-detail.model';
import { IQuotaService } from 'app/entities/quota-service/quota-service.model';

export interface IConsommationQuota {
  id: string;
  quantite?: number | null;
  dateConsommation?: dayjs.Dayjs | null;
  client?: Pick<IClient, 'id' | 'numero'> | null;
  quotaService?: Pick<IQuotaService, 'id'> | null;
  quotaDetail?: Pick<IQuotaDetail, 'id'> | null;
}

export type NewConsommationQuota = Omit<IConsommationQuota, 'id'> & { id: null };
