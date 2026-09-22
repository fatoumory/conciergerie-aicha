import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IQuotaService } from '../quota-service.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../quota-service.test-samples';

import { QuotaServiceService, RestQuotaService } from './quota-service.service';

const requireRestSample: RestQuotaService = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('QuotaService Service', () => {
  let service: QuotaServiceService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuotaService | IQuotaService[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuotaServiceService);
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

    it('should create a QuotaService', () => {
      const quotaService = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quotaService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuotaService', () => {
      const quotaService = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quotaService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuotaService', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuotaService', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuotaService', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addQuotaServiceToCollectionIfMissing', () => {
      it('should add a QuotaService to an empty array', () => {
        const quotaService: IQuotaService = sampleWithRequiredData;
        expectedResult = service.addQuotaServiceToCollectionIfMissing([], quotaService);
        expect(expectedResult).toEqual([quotaService]);
      });

      it('should not add a QuotaService to an array that contains it', () => {
        const quotaService: IQuotaService = sampleWithRequiredData;
        const quotaServiceCollection: IQuotaService[] = [
          {
            ...quotaService,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuotaServiceToCollectionIfMissing(quotaServiceCollection, quotaService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuotaService to an array that doesn't contain it", () => {
        const quotaService: IQuotaService = sampleWithRequiredData;
        const quotaServiceCollection: IQuotaService[] = [sampleWithPartialData];
        expectedResult = service.addQuotaServiceToCollectionIfMissing(quotaServiceCollection, quotaService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quotaService);
      });

      it('should add only unique QuotaService to an array', () => {
        const quotaServiceArray: IQuotaService[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quotaServiceCollection: IQuotaService[] = [sampleWithRequiredData];
        expectedResult = service.addQuotaServiceToCollectionIfMissing(quotaServiceCollection, ...quotaServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quotaService: IQuotaService = sampleWithRequiredData;
        const quotaService2: IQuotaService = sampleWithPartialData;
        expectedResult = service.addQuotaServiceToCollectionIfMissing([], quotaService, quotaService2);
        expect(expectedResult).toEqual([quotaService, quotaService2]);
      });

      it('should accept null and undefined values', () => {
        const quotaService: IQuotaService = sampleWithRequiredData;
        expectedResult = service.addQuotaServiceToCollectionIfMissing([], null, quotaService, undefined);
        expect(expectedResult).toEqual([quotaService]);
      });

      it('should return initial array if no QuotaService is added', () => {
        const quotaServiceCollection: IQuotaService[] = [sampleWithRequiredData];
        expectedResult = service.addQuotaServiceToCollectionIfMissing(quotaServiceCollection, undefined, null);
        expect(expectedResult).toEqual(quotaServiceCollection);
      });
    });

    describe('compareQuotaService', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuotaService(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
        const entity2 = null;

        const compareResult1 = service.compareQuotaService(entity1, entity2);
        const compareResult2 = service.compareQuotaService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
        const entity2 = { id: '7f8f19e3-0d4a-4d13-b572-5b14477e5d1d' };

        const compareResult1 = service.compareQuotaService(entity1, entity2);
        const compareResult2 = service.compareQuotaService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
        const entity2 = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };

        const compareResult1 = service.compareQuotaService(entity1, entity2);
        const compareResult2 = service.compareQuotaService(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
