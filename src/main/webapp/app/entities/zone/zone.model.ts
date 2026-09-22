export interface IZone {
  id: string;
  code?: string | null;
  libelle?: string | null;
}

export type NewZone = Omit<IZone, 'id'> & { id: null };
