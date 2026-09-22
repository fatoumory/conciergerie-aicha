export interface ITypeDemande {
  id: string;
  code?: string | null;
  libelle?: string | null;
}

export type NewTypeDemande = Omit<ITypeDemande, 'id'> & { id: null };
