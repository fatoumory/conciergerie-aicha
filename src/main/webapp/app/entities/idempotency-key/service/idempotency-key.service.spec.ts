import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IIdempotencyKey } from '../idempotency-key.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../idempotency-key.test-samples';

import { IdempotencyKeyService, RestIdempotencyKey } from './idempotency-key.service';

const requireRestSample: RestIdempotencyKey = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.toJSON(),
  dateExpiration: sampleWithRequiredData.dateExpiration?.toJSON(),
};

describe('IdempotencyKey Service', () => {
  let service: IdempotencyKeyService;
  let httpMock: HttpTestingController;
  let expectedResult: IIdempotencyKey | IIdempotencyKey[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(IdempotencyKeyService);
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

    it('should create a IdempotencyKey', () => {
      const idempotencyKey = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(idempotencyKey).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IdempotencyKey', () => {
      const idempotencyKey = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(idempotencyKey).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IdempotencyKey', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IdempotencyKey', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IdempotencyKey', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addIdempotencyKeyToCollectionIfMissing', () => {
      it('should add a IdempotencyKey to an empty array', () => {
        const idempotencyKey: IIdempotencyKey = sampleWithRequiredData;
        expectedResult = service.addIdempotencyKeyToCollectionIfMissing([], idempotencyKey);
        expect(expectedResult).toEqual([idempotencyKey]);
      });

      it('should not add a IdempotencyKey to an array that contains it', () => {
        const idempotencyKey: IIdempotencyKey = sampleWithRequiredData;
        const idempotencyKeyCollection: IIdempotencyKey[] = [
          {
            ...idempotencyKey,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIdempotencyKeyToCollectionIfMissing(idempotencyKeyCollection, idempotencyKey);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IdempotencyKey to an array that doesn't contain it", () => {
        const idempotencyKey: IIdempotencyKey = sampleWithRequiredData;
        const idempotencyKeyCollection: IIdempotencyKey[] = [sampleWithPartialData];
        expectedResult = service.addIdempotencyKeyToCollectionIfMissing(idempotencyKeyCollection, idempotencyKey);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(idempotencyKey);
      });

      it('should add only unique IdempotencyKey to an array', () => {
        const idempotencyKeyArray: IIdempotencyKey[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const idempotencyKeyCollection: IIdempotencyKey[] = [sampleWithRequiredData];
        expectedResult = service.addIdempotencyKeyToCollectionIfMissing(idempotencyKeyCollection, ...idempotencyKeyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const idempotencyKey: IIdempotencyKey = sampleWithRequiredData;
        const idempotencyKey2: IIdempotencyKey = sampleWithPartialData;
        expectedResult = service.addIdempotencyKeyToCollectionIfMissing([], idempotencyKey, idempotencyKey2);
        expect(expectedResult).toEqual([idempotencyKey, idempotencyKey2]);
      });

      it('should accept null and undefined values', () => {
        const idempotencyKey: IIdempotencyKey = sampleWithRequiredData;
        expectedResult = service.addIdempotencyKeyToCollectionIfMissing([], null, idempotencyKey, undefined);
        expect(expectedResult).toEqual([idempotencyKey]);
      });

      it('should return initial array if no IdempotencyKey is added', () => {
        const idempotencyKeyCollection: IIdempotencyKey[] = [sampleWithRequiredData];
        expectedResult = service.addIdempotencyKeyToCollectionIfMissing(idempotencyKeyCollection, undefined, null);
        expect(expectedResult).toEqual(idempotencyKeyCollection);
      });
    });

    describe('compareIdempotencyKey', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIdempotencyKey(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' };
        const entity2 = null;

        const compareResult1 = service.compareIdempotencyKey(entity1, entity2);
        const compareResult2 = service.compareIdempotencyKey(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' };
        const entity2 = { id: 'c81b889c-e72a-4127-89b5-2c11138dd9d2' };

        const compareResult1 = service.compareIdempotencyKey(entity1, entity2);
        const compareResult2 = service.compareIdempotencyKey(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' };
        const entity2 = { id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' };

        const compareResult1 = service.compareIdempotencyKey(entity1, entity2);
        const compareResult2 = service.compareIdempotencyKey(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
