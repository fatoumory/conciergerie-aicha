import { ISegmentClient } from 'app/entities/segment-client/segment-client.model';
import { ITypeClient } from 'app/entities/type-client/type-client.model';
import { IUser } from 'app/entities/user/user.model';

export interface IClient {
  id: string;
  numero?: string | null;
  prenom?: string | null;
  nom?: string | null;
  email?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  typeClient?: Pick<ITypeClient, 'id' | 'libelle'> | null;
  segmentClient?: Pick<ISegmentClient, 'id' | 'libelle'> | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
