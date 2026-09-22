import { IQuotaDetail, NewQuotaDetail } from './quota-detail.model';

export const sampleWithRequiredData: IQuotaDetail = {
  id: '0055525b-3668-40a3-aadd-351c0d1995d4',
  limite: 25939.41,
};

export const sampleWithPartialData: IQuotaDetail = {
  id: '91274001-7ccd-4762-b3bf-1d7da3da4dfd',
  limite: 26699.78,
};

export const sampleWithFullData: IQuotaDetail = {
  id: '466173b5-50be-4228-8123-7df8dd6fa53b',
  limite: 17099.89,
};

export const sampleWithNewData: NewQuotaDetail = {
  limite: 20021.5,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
