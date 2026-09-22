export interface IPartenaire {
  id: string;
  code?: string | null;
  libelle?: string | null;
}

export type NewPartenaire = Omit<IPartenaire, 'id'> & { id: null };
