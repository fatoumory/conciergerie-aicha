import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITypeClient } from '../type-client.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../type-client.test-samples';

import { TypeClientService } from './type-client.service';

const requireRestSample: ITypeClient = {
  ...sampleWithRequiredData,
};

describe('TypeClient Service', () => {
  let service: TypeClientService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeClient | ITypeClient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TypeClientService);
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

    it('should create a TypeClient', () => {
      const typeClient = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeClient).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeClient', () => {
      const typeClient = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeClient).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeClient', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeClient', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeClient', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTypeClientToCollectionIfMissing', () => {
      it('should add a TypeClient to an empty array', () => {
        const typeClient: ITypeClient = sampleWithRequiredData;
        expectedResult = service.addTypeClientToCollectionIfMissing([], typeClient);
        expect(expectedResult).toEqual([typeClient]);
      });

      it('should not add a TypeClient to an array that contains it', () => {
        const typeClient: ITypeClient = sampleWithRequiredData;
        const typeClientCollection: ITypeClient[] = [
          {
            ...typeClient,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeClientToCollectionIfMissing(typeClientCollection, typeClient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeClient to an array that doesn't contain it", () => {
        const typeClient: ITypeClient = sampleWithRequiredData;
        const typeClientCollection: ITypeClient[] = [sampleWithPartialData];
        expectedResult = service.addTypeClientToCollectionIfMissing(typeClientCollection, typeClient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeClient);
      });

      it('should add only unique TypeClient to an array', () => {
        const typeClientArray: ITypeClient[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeClientCollection: ITypeClient[] = [sampleWithRequiredData];
        expectedResult = service.addTypeClientToCollectionIfMissing(typeClientCollection, ...typeClientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeClient: ITypeClient = sampleWithRequiredData;
        const typeClient2: ITypeClient = sampleWithPartialData;
        expectedResult = service.addTypeClientToCollectionIfMissing([], typeClient, typeClient2);
        expect(expectedResult).toEqual([typeClient, typeClient2]);
      });

      it('should accept null and undefined values', () => {
        const typeClient: ITypeClient = sampleWithRequiredData;
        expectedResult = service.addTypeClientToCollectionIfMissing([], null, typeClient, undefined);
        expect(expectedResult).toEqual([typeClient]);
      });

      it('should return initial array if no TypeClient is added', () => {
        const typeClientCollection: ITypeClient[] = [sampleWithRequiredData];
        expectedResult = service.addTypeClientToCollectionIfMissing(typeClientCollection, undefined, null);
        expect(expectedResult).toEqual(typeClientCollection);
      });
    });

    describe('compareTypeClient', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeClient(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
        const entity2 = null;

        const compareResult1 = service.compareTypeClient(entity1, entity2);
        const compareResult2 = service.compareTypeClient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
        const entity2 = { id: 'c3bcad5f-3b94-4cfe-b1a6-4c3b86b45e40' };

        const compareResult1 = service.compareTypeClient(entity1, entity2);
        const compareResult2 = service.compareTypeClient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
        const entity2 = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };

        const compareResult1 = service.compareTypeClient(entity1, entity2);
        const compareResult2 = service.compareTypeClient(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
