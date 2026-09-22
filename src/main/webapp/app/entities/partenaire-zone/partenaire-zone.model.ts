import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { IZone } from 'app/entities/zone/zone.model';

export interface IPartenaireZone {
  id: string;
  partenaire?: Pick<IPartenaire, 'id' | 'libelle'> | null;
  zone?: Pick<IZone, 'id' | 'libelle'> | null;
}

export type NewPartenaireZone = Omit<IPartenaireZone, 'id'> & { id: null };
