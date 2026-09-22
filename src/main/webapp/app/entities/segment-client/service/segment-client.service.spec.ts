import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ISegmentClient } from '../segment-client.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../segment-client.test-samples';

import { SegmentClientService } from './segment-client.service';

const requireRestSample: ISegmentClient = {
  ...sampleWithRequiredData,
};

describe('SegmentClient Service', () => {
  let service: SegmentClientService;
  let httpMock: HttpTestingController;
  let expectedResult: ISegmentClient | ISegmentClient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SegmentClientService);
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

    it('should create a SegmentClient', () => {
      const segmentClient = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(segmentClient).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SegmentClient', () => {
      const segmentClient = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(segmentClient).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SegmentClient', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SegmentClient', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SegmentClient', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addSegmentClientToCollectionIfMissing', () => {
      it('should add a SegmentClient to an empty array', () => {
        const segmentClient: ISegmentClient = sampleWithRequiredData;
        expectedResult = service.addSegmentClientToCollectionIfMissing([], segmentClient);
        expect(expectedResult).toEqual([segmentClient]);
      });

      it('should not add a SegmentClient to an array that contains it', () => {
        const segmentClient: ISegmentClient = sampleWithRequiredData;
        const segmentClientCollection: ISegmentClient[] = [
          {
            ...segmentClient,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSegmentClientToCollectionIfMissing(segmentClientCollection, segmentClient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SegmentClient to an array that doesn't contain it", () => {
        const segmentClient: ISegmentClient = sampleWithRequiredData;
        const segmentClientCollection: ISegmentClient[] = [sampleWithPartialData];
        expectedResult = service.addSegmentClientToCollectionIfMissing(segmentClientCollection, segmentClient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(segmentClient);
      });

      it('should add only unique SegmentClient to an array', () => {
        const segmentClientArray: ISegmentClient[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const segmentClientCollection: ISegmentClient[] = [sampleWithRequiredData];
        expectedResult = service.addSegmentClientToCollectionIfMissing(segmentClientCollection, ...segmentClientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const segmentClient: ISegmentClient = sampleWithRequiredData;
        const segmentClient2: ISegmentClient = sampleWithPartialData;
        expectedResult = service.addSegmentClientToCollectionIfMissing([], segmentClient, segmentClient2);
        expect(expectedResult).toEqual([segmentClient, segmentClient2]);
      });

      it('should accept null and undefined values', () => {
        const segmentClient: ISegmentClient = sampleWithRequiredData;
        expectedResult = service.addSegmentClientToCollectionIfMissing([], null, segmentClient, undefined);
        expect(expectedResult).toEqual([segmentClient]);
      });

      it('should return initial array if no SegmentClient is added', () => {
        const segmentClientCollection: ISegmentClient[] = [sampleWithRequiredData];
        expectedResult = service.addSegmentClientToCollectionIfMissing(segmentClientCollection, undefined, null);
        expect(expectedResult).toEqual(segmentClientCollection);
      });
    });

    describe('compareSegmentClient', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSegmentClient(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
        const entity2 = null;

        const compareResult1 = service.compareSegmentClient(entity1, entity2);
        const compareResult2 = service.compareSegmentClient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
        const entity2 = { id: '3dfe0be4-098d-4b25-9305-36dbaeacd2d1' };

        const compareResult1 = service.compareSegmentClient(entity1, entity2);
        const compareResult2 = service.compareSegmentClient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
        const entity2 = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };

        const compareResult1 = service.compareSegmentClient(entity1, entity2);
        const compareResult2 = service.compareSegmentClient(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
