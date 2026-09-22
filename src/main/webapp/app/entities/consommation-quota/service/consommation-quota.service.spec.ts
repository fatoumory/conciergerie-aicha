import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IConsommationQuota } from '../consommation-quota.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../consommation-quota.test-samples';

import { ConsommationQuotaService, RestConsommationQuota } from './consommation-quota.service';

const requireRestSample: RestConsommationQuota = {
  ...sampleWithRequiredData,
  dateConsommation: sampleWithRequiredData.dateConsommation?.toJSON(),
};

describe('ConsommationQuota Service', () => {
  let service: ConsommationQuotaService;
  let httpMock: HttpTestingController;
  let expectedResult: IConsommationQuota | IConsommationQuota[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ConsommationQuotaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ConsommationQuota', () => {
      const consommationQuota = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(consommationQuota).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConsommationQuota', () => {
      const consommationQuota = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(consommationQuota).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConsommationQuota', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConsommationQuota', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConsommationQuota', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addConsommationQuotaToCollectionIfMissing', () => {
      it('should add a ConsommationQuota to an empty array', () => {
        const consommationQuota: IConsommationQuota = sampleWithRequiredData;
        expectedResult = service.addConsommationQuotaToCollectionIfMissing([], consommationQuota);
        expect(expectedResult).toEqual([consommationQuota]);
      });

      it('should not add a ConsommationQuota to an array that contains it', () => {
        const consommationQuota: IConsommationQuota = sampleWithRequiredData;
        const consommationQuotaCollection: IConsommationQuota[] = [
          {
            ...consommationQuota,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConsommationQuotaToCollectionIfMissing(consommationQuotaCollection, consommationQuota);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConsommationQuota to an array that doesn't contain it", () => {
        const consommationQuota: IConsommationQuota = sampleWithRequiredData;
        const consommationQuotaCollection: IConsommationQuota[] = [sampleWithPartialData];
        expectedResult = service.addConsommationQuotaToCollectionIfMissing(consommationQuotaCollection, consommationQuota);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consommationQuota);
      });

      it('should add only unique ConsommationQuota to an array', () => {
        const consommationQuotaArray: IConsommationQuota[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const consommationQuotaCollection: IConsommationQuota[] = [sampleWithRequiredData];
        expectedResult = service.addConsommationQuotaToCollectionIfMissing(consommationQuotaCollection, ...consommationQuotaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const consommationQuota: IConsommationQuota = sampleWithRequiredData;
        const consommationQuota2: IConsommationQuota = sampleWithPartialData;
        expectedResult = service.addConsommationQuotaToCollectionIfMissing([], consommationQuota, consommationQuota2);
        expect(expectedResult).toEqual([consommationQuota, consommationQuota2]);
      });

      it('should accept null and undefined values', () => {
        const consommationQuota: IConsommationQuota = sampleWithRequiredData;
        expectedResult = service.addConsommationQuotaToCollectionIfMissing([], null, consommationQuota, undefined);
        expect(expectedResult).toEqual([consommationQuota]);
      });

      it('should return initial array if no ConsommationQuota is added', () => {
        const consommationQuotaCollection: IConsommationQuota[] = [sampleWithRequiredData];
        expectedResult = service.addConsommationQuotaToCollectionIfMissing(consommationQuotaCollection, undefined, null);
        expect(expectedResult).toEqual(consommationQuotaCollection);
      });
    });

    describe('compareConsommationQuota', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConsommationQuota(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' };
        const entity2 = null;

        const compareResult1 = service.compareConsommationQuota(entity1, entity2);
        const compareResult2 = service.compareConsommationQuota(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' };
        const entity2 = { id: '8ed81fb1-e5e8-41ee-a12e-261e410b8c2a' };

        const compareResult1 = service.compareConsommationQuota(entity1, entity2);
        const compareResult2 = service.compareConsommationQuota(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' };
        const entity2 = { id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' };

        const compareResult1 = service.compareConsommationQuota(entity1, entity2);
        const compareResult2 = service.compareConsommationQuota(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
