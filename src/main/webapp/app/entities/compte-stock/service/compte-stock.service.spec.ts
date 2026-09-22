import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICompteStock } from '../compte-stock.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../compte-stock.test-samples';

import { CompteStockService } from './compte-stock.service';

const requireRestSample: ICompteStock = {
  ...sampleWithRequiredData,
};

describe('CompteStock Service', () => {
  let service: CompteStockService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompteStock | ICompteStock[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CompteStockService);
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

    it('should create a CompteStock', () => {
      const compteStock = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compteStock).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompteStock', () => {
      const compteStock = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compteStock).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompteStock', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompteStock', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompteStock', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCompteStockToCollectionIfMissing', () => {
      it('should add a CompteStock to an empty array', () => {
        const compteStock: ICompteStock = sampleWithRequiredData;
        expectedResult = service.addCompteStockToCollectionIfMissing([], compteStock);
        expect(expectedResult).toEqual([compteStock]);
      });

      it('should not add a CompteStock to an array that contains it', () => {
        const compteStock: ICompteStock = sampleWithRequiredData;
        const compteStockCollection: ICompteStock[] = [
          {
            ...compteStock,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompteStockToCollectionIfMissing(compteStockCollection, compteStock);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompteStock to an array that doesn't contain it", () => {
        const compteStock: ICompteStock = sampleWithRequiredData;
        const compteStockCollection: ICompteStock[] = [sampleWithPartialData];
        expectedResult = service.addCompteStockToCollectionIfMissing(compteStockCollection, compteStock);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compteStock);
      });

      it('should add only unique CompteStock to an array', () => {
        const compteStockArray: ICompteStock[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compteStockCollection: ICompteStock[] = [sampleWithRequiredData];
        expectedResult = service.addCompteStockToCollectionIfMissing(compteStockCollection, ...compteStockArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compteStock: ICompteStock = sampleWithRequiredData;
        const compteStock2: ICompteStock = sampleWithPartialData;
        expectedResult = service.addCompteStockToCollectionIfMissing([], compteStock, compteStock2);
        expect(expectedResult).toEqual([compteStock, compteStock2]);
      });

      it('should accept null and undefined values', () => {
        const compteStock: ICompteStock = sampleWithRequiredData;
        expectedResult = service.addCompteStockToCollectionIfMissing([], null, compteStock, undefined);
        expect(expectedResult).toEqual([compteStock]);
      });

      it('should return initial array if no CompteStock is added', () => {
        const compteStockCollection: ICompteStock[] = [sampleWithRequiredData];
        expectedResult = service.addCompteStockToCollectionIfMissing(compteStockCollection, undefined, null);
        expect(expectedResult).toEqual(compteStockCollection);
      });
    });

    describe('compareCompteStock', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompteStock(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
        const entity2 = null;

        const compareResult1 = service.compareCompteStock(entity1, entity2);
        const compareResult2 = service.compareCompteStock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
        const entity2 = { id: 'f4e6718c-f6ee-4140-947c-bdbdef1eda55' };

        const compareResult1 = service.compareCompteStock(entity1, entity2);
        const compareResult2 = service.compareCompteStock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
        const entity2 = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };

        const compareResult1 = service.compareCompteStock(entity1, entity2);
        const compareResult2 = service.compareCompteStock(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
