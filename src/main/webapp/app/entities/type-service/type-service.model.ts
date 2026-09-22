export interface ITypeService {
  id: string;
  code?: string | null;
  libelle?: string | null;
}

export type NewTypeService = Omit<ITypeService, 'id'> & { id: null };
