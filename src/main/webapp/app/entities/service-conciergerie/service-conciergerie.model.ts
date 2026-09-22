import { ITypeService } from 'app/entities/type-service/type-service.model';

export interface IServiceConciergerie {
  id: string;
  code?: string | null;
  libelle?: string | null;
  description?: string | null;
  typeService?: Pick<ITypeService, 'id' | 'libelle'> | null;
}

export type NewServiceConciergerie = Omit<IServiceConciergerie, 'id'> & { id: null };
