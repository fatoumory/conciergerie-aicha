import { IPartenaireZone, NewPartenaireZone } from './partenaire-zone.model';

export const sampleWithRequiredData: IPartenaireZone = {
  id: '1d026089-32a0-4a03-b5dd-b57daad48fc8',
};

export const sampleWithPartialData: IPartenaireZone = {
  id: '750f15ab-880b-4ae7-abd4-8a0bbd89830f',
};

export const sampleWithFullData: IPartenaireZone = {
  id: '30fbbddd-9f1d-403e-8265-484ccf19009d',
};

export const sampleWithNewData: NewPartenaireZone = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
