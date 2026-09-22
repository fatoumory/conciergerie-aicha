import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITransactionPaiement } from '../transaction-paiement.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../transaction-paiement.test-samples';

import { RestTransactionPaiement, TransactionPaiementService } from './transaction-paiement.service';

const requireRestSample: RestTransactionPaiement = {
  ...sampleWithRequiredData,
  dateTransaction: sampleWithRequiredData.dateTransaction?.toJSON(),
};

describe('TransactionPaiement Service', () => {
  let service: TransactionPaiementService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransactionPaiement | ITransactionPaiement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransactionPaiementService);
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

    it('should create a TransactionPaiement', () => {
      const transactionPaiement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transactionPaiement).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransactionPaiement', () => {
      const transactionPaiement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transactionPaiement).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransactionPaiement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransactionPaiement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransactionPaiement', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTransactionPaiementToCollectionIfMissing', () => {
      it('should add a TransactionPaiement to an empty array', () => {
        const transactionPaiement: ITransactionPaiement = sampleWithRequiredData;
        expectedResult = service.addTransactionPaiementToCollectionIfMissing([], transactionPaiement);
        expect(expectedResult).toEqual([transactionPaiement]);
      });

      it('should not add a TransactionPaiement to an array that contains it', () => {
        const transactionPaiement: ITransactionPaiement = sampleWithRequiredData;
        const transactionPaiementCollection: ITransactionPaiement[] = [
          {
            ...transactionPaiement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransactionPaiementToCollectionIfMissing(transactionPaiementCollection, transactionPaiement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransactionPaiement to an array that doesn't contain it", () => {
        const transactionPaiement: ITransactionPaiement = sampleWithRequiredData;
        const transactionPaiementCollection: ITransactionPaiement[] = [sampleWithPartialData];
        expectedResult = service.addTransactionPaiementToCollectionIfMissing(transactionPaiementCollection, transactionPaiement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transactionPaiement);
      });

      it('should add only unique TransactionPaiement to an array', () => {
        const transactionPaiementArray: ITransactionPaiement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transactionPaiementCollection: ITransactionPaiement[] = [sampleWithRequiredData];
        expectedResult = service.addTransactionPaiementToCollectionIfMissing(transactionPaiementCollection, ...transactionPaiementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transactionPaiement: ITransactionPaiement = sampleWithRequiredData;
        const transactionPaiement2: ITransactionPaiement = sampleWithPartialData;
        expectedResult = service.addTransactionPaiementToCollectionIfMissing([], transactionPaiement, transactionPaiement2);
        expect(expectedResult).toEqual([transactionPaiement, transactionPaiement2]);
      });

      it('should accept null and undefined values', () => {
        const transactionPaiement: ITransactionPaiement = sampleWithRequiredData;
        expectedResult = service.addTransactionPaiementToCollectionIfMissing([], null, transactionPaiement, undefined);
        expect(expectedResult).toEqual([transactionPaiement]);
      });

      it('should return initial array if no TransactionPaiement is added', () => {
        const transactionPaiementCollection: ITransactionPaiement[] = [sampleWithRequiredData];
        expectedResult = service.addTransactionPaiementToCollectionIfMissing(transactionPaiementCollection, undefined, null);
        expect(expectedResult).toEqual(transactionPaiementCollection);
      });
    });

    describe('compareTransactionPaiement', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransactionPaiement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '33f8355d-c755-4a93-9672-44338f6f0121' };
        const entity2 = null;

        const compareResult1 = service.compareTransactionPaiement(entity1, entity2);
        const compareResult2 = service.compareTransactionPaiement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '33f8355d-c755-4a93-9672-44338f6f0121' };
        const entity2 = { id: '0057cb97-0599-4416-8446-1654644a3cfd' };

        const compareResult1 = service.compareTransactionPaiement(entity1, entity2);
        const compareResult2 = service.compareTransactionPaiement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '33f8355d-c755-4a93-9672-44338f6f0121' };
        const entity2 = { id: '33f8355d-c755-4a93-9672-44338f6f0121' };

        const compareResult1 = service.compareTransactionPaiement(entity1, entity2);
        const compareResult2 = service.compareTransactionPaiement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
