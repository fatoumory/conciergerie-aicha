import { ISegmentClient, NewSegmentClient } from './segment-client.model';

export const sampleWithRequiredData: ISegmentClient = {
  id: '3dea0846-1da2-4ec3-b2ac-de2e4c217740',
  code: 'avant que raide athlète',
  libelle: 'si',
};

export const sampleWithPartialData: ISegmentClient = {
  id: '598d9c3f-e57d-41d7-8fbc-519fa168438c',
  code: 'a adversaire',
  libelle: 'où cadre',
};

export const sampleWithFullData: ISegmentClient = {
  id: 'db1ad9f6-fd88-47eb-b7a1-0f35970c956a',
  code: 'résigner mairie à moins de',
  libelle: 'très',
};

export const sampleWithNewData: NewSegmentClient = {
  code: 'crac contrôler',
  libelle: 'croâ dynamique ensuite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
