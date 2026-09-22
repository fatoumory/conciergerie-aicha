import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IQuotaDetail } from '../quota-detail.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../quota-detail.test-samples';

import { QuotaDetailService } from './quota-detail.service';

const requireRestSample: IQuotaDetail = {
  ...sampleWithRequiredData,
};

describe('QuotaDetail Service', () => {
  let service: QuotaDetailService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuotaDetail | IQuotaDetail[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuotaDetailService);
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

    it('should create a QuotaDetail', () => {
      const quotaDetail = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quotaDetail).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuotaDetail', () => {
      const quotaDetail = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quotaDetail).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuotaDetail', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuotaDetail', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuotaDetail', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addQuotaDetailToCollectionIfMissing', () => {
      it('should add a QuotaDetail to an empty array', () => {
        const quotaDetail: IQuotaDetail = sampleWithRequiredData;
        expectedResult = service.addQuotaDetailToCollectionIfMissing([], quotaDetail);
        expect(expectedResult).toEqual([quotaDetail]);
      });

      it('should not add a QuotaDetail to an array that contains it', () => {
        const quotaDetail: IQuotaDetail = sampleWithRequiredData;
        const quotaDetailCollection: IQuotaDetail[] = [
          {
            ...quotaDetail,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuotaDetailToCollectionIfMissing(quotaDetailCollection, quotaDetail);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuotaDetail to an array that doesn't contain it", () => {
        const quotaDetail: IQuotaDetail = sampleWithRequiredData;
        const quotaDetailCollection: IQuotaDetail[] = [sampleWithPartialData];
        expectedResult = service.addQuotaDetailToCollectionIfMissing(quotaDetailCollection, quotaDetail);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quotaDetail);
      });

      it('should add only unique QuotaDetail to an array', () => {
        const quotaDetailArray: IQuotaDetail[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quotaDetailCollection: IQuotaDetail[] = [sampleWithRequiredData];
        expectedResult = service.addQuotaDetailToCollectionIfMissing(quotaDetailCollection, ...quotaDetailArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quotaDetail: IQuotaDetail = sampleWithRequiredData;
        const quotaDetail2: IQuotaDetail = sampleWithPartialData;
        expectedResult = service.addQuotaDetailToCollectionIfMissing([], quotaDetail, quotaDetail2);
        expect(expectedResult).toEqual([quotaDetail, quotaDetail2]);
      });

      it('should accept null and undefined values', () => {
        const quotaDetail: IQuotaDetail = sampleWithRequiredData;
        expectedResult = service.addQuotaDetailToCollectionIfMissing([], null, quotaDetail, undefined);
        expect(expectedResult).toEqual([quotaDetail]);
      });

      it('should return initial array if no QuotaDetail is added', () => {
        const quotaDetailCollection: IQuotaDetail[] = [sampleWithRequiredData];
        expectedResult = service.addQuotaDetailToCollectionIfMissing(quotaDetailCollection, undefined, null);
        expect(expectedResult).toEqual(quotaDetailCollection);
      });
    });

    describe('compareQuotaDetail', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuotaDetail(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
        const entity2 = null;

        const compareResult1 = service.compareQuotaDetail(entity1, entity2);
        const compareResult2 = service.compareQuotaDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
        const entity2 = { id: 'dcc90518-4e9b-457c-9452-a4b8a2b9176b' };

        const compareResult1 = service.compareQuotaDetail(entity1, entity2);
        const compareResult2 = service.compareQuotaDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
        const entity2 = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };

        const compareResult1 = service.compareQuotaDetail(entity1, entity2);
        const compareResult2 = service.compareQuotaDetail(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
