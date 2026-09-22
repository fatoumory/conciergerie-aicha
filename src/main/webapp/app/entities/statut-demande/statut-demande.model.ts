export interface IStatutDemande {
  id: string;
  code?: string | null;
  libelle?: string | null;
}

export type NewStatutDemande = Omit<IStatutDemande, 'id'> & { id: null };
