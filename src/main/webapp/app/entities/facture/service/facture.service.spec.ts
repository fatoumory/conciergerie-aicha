import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacture } from '../facture.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../facture.test-samples';

import { FactureService, RestFacture } from './facture.service';

const requireRestSample: RestFacture = {
  ...sampleWithRequiredData,
  dateGeneration: sampleWithRequiredData.dateGeneration?.toJSON(),
};

describe('Facture Service', () => {
  let service: FactureService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacture | IFacture[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactureService);
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

    it('should create a Facture', () => {
      const facture = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facture).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Facture', () => {
      const facture = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facture).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Facture', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Facture', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Facture', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFactureToCollectionIfMissing', () => {
      it('should add a Facture to an empty array', () => {
        const facture: IFacture = sampleWithRequiredData;
        expectedResult = service.addFactureToCollectionIfMissing([], facture);
        expect(expectedResult).toEqual([facture]);
      });

      it('should not add a Facture to an array that contains it', () => {
        const facture: IFacture = sampleWithRequiredData;
        const factureCollection: IFacture[] = [
          {
            ...facture,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactureToCollectionIfMissing(factureCollection, facture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Facture to an array that doesn't contain it", () => {
        const facture: IFacture = sampleWithRequiredData;
        const factureCollection: IFacture[] = [sampleWithPartialData];
        expectedResult = service.addFactureToCollectionIfMissing(factureCollection, facture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facture);
      });

      it('should add only unique Facture to an array', () => {
        const factureArray: IFacture[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factureCollection: IFacture[] = [sampleWithRequiredData];
        expectedResult = service.addFactureToCollectionIfMissing(factureCollection, ...factureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facture: IFacture = sampleWithRequiredData;
        const facture2: IFacture = sampleWithPartialData;
        expectedResult = service.addFactureToCollectionIfMissing([], facture, facture2);
        expect(expectedResult).toEqual([facture, facture2]);
      });

      it('should accept null and undefined values', () => {
        const facture: IFacture = sampleWithRequiredData;
        expectedResult = service.addFactureToCollectionIfMissing([], null, facture, undefined);
        expect(expectedResult).toEqual([facture]);
      });

      it('should return initial array if no Facture is added', () => {
        const factureCollection: IFacture[] = [sampleWithRequiredData];
        expectedResult = service.addFactureToCollectionIfMissing(factureCollection, undefined, null);
        expect(expectedResult).toEqual(factureCollection);
      });
    });

    describe('compareFacture', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacture(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' };
        const entity2 = null;

        const compareResult1 = service.compareFacture(entity1, entity2);
        const compareResult2 = service.compareFacture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' };
        const entity2 = { id: '51300b25-7f46-437f-9213-a92091e89ea4' };

        const compareResult1 = service.compareFacture(entity1, entity2);
        const compareResult2 = service.compareFacture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' };
        const entity2 = { id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' };

        const compareResult1 = service.compareFacture(entity1, entity2);
        const compareResult2 = service.compareFacture(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
