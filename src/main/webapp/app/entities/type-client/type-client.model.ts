export interface ITypeClient {
  id: string;
  code?: string | null;
  libelle?: string | null;
}

export type NewTypeClient = Omit<ITypeClient, 'id'> & { id: null };
