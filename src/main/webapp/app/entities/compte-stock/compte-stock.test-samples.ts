import { ICompteStock, NewCompteStock } from './compte-stock.model';

export const sampleWithRequiredData: ICompteStock = {
  id: '9fb58fcf-4521-4278-ba90-ca592c8f912e',
  solde: 27779.23,
};

export const sampleWithPartialData: ICompteStock = {
  id: '3a2c2c13-258f-421a-8846-4c1a9f4d1a2d',
  solde: 9718.41,
};

export const sampleWithFullData: ICompteStock = {
  id: 'b8f2def2-b132-4d2b-9a1e-b7f44b9afbde',
  solde: 4545.7,
};

export const sampleWithNewData: NewCompteStock = {
  solde: 15435.09,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
