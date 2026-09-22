export interface ISegmentClient {
  id: string;
  code?: string | null;
  libelle?: string | null;
}

export type NewSegmentClient = Omit<ISegmentClient, 'id'> & { id: null };
