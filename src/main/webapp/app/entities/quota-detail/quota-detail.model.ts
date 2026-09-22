import { IQuotaService } from 'app/entities/quota-service/quota-service.model';
import { IZone } from 'app/entities/zone/zone.model';

export interface IQuotaDetail {
  id: string;
  limite?: number | null;
  quotaService?: Pick<IQuotaService, 'id'> | null;
  zone?: Pick<IZone, 'id' | 'libelle'> | null;
}

export type NewQuotaDetail = Omit<IQuotaDetail, 'id'> & { id: null };
