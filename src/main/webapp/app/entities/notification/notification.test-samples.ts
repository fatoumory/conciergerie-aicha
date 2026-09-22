import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: '4917d9fb-b873-41d6-981d-153319417462',
  titre: 'prou croâ',
  message: '../fake-data/blob/hipster.txt',
  dateEnvoi: dayjs('2026-09-22T05:19'),
  lu: false,
};

export const sampleWithPartialData: INotification = {
  id: 'dc0901a9-6b66-4127-b03c-b490129d6818',
  titre: 'échouer super incalculable',
  message: '../fake-data/blob/hipster.txt',
  dateEnvoi: dayjs('2026-09-21T12:36'),
  lu: true,
};

export const sampleWithFullData: INotification = {
  id: '27befbee-c69c-445f-a315-e759c182bfa5',
  titre: 'excuser',
  message: '../fake-data/blob/hipster.txt',
  dateEnvoi: dayjs('2026-09-21T20:20'),
  lu: false,
};

export const sampleWithNewData: NewNotification = {
  titre: 'de peur que en vérité coin-coin',
  message: '../fake-data/blob/hipster.txt',
  dateEnvoi: dayjs('2026-09-22T08:08'),
  lu: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
