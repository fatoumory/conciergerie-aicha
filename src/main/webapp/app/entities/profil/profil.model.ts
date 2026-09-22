import { IAuthority } from 'app/entities/admin/authority/authority.model';
import { IUser } from 'app/entities/user/user.model';

export interface IProfil {
  id: string;
  code?: string | null;
  libelle?: string | null;
  description?: string | null;
  utilisateurs?: Pick<IUser, 'id' | 'login'>[] | null;
  roles?: Pick<IAuthority, 'name'>[] | null;
}

export type NewProfil = Omit<IProfil, 'id'> & { id: null };
