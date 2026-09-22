import { ITypeService, NewTypeService } from './type-service.model';

export const sampleWithRequiredData: ITypeService = {
  id: '1622c7de-036c-4060-9914-65566aa4c589',
  code: 'coac coac',
  libelle: 'mince collègue',
};

export const sampleWithPartialData: ITypeService = {
  id: 'f89273ba-dd01-4379-836d-7ab436a39c66',
  code: 'à raison de commencer préparer',
  libelle: 'alors que viser horrible',
};

export const sampleWithFullData: ITypeService = {
  id: '825a6d32-21a8-4e30-9319-529febd11878',
  code: "à l'instar de à côté de",
  libelle: 'foule afficher',
};

export const sampleWithNewData: NewTypeService = {
  code: 'pourvu que clac assurément',
  libelle: 'direction',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
