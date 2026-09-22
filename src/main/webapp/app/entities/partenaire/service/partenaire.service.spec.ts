import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartenaire } from '../partenaire.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../partenaire.test-samples';

import { PartenaireService } from './partenaire.service';

const requireRestSample: IPartenaire = {
  ...sampleWithRequiredData,
};

describe('Partenaire Service', () => {
  let service: PartenaireService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartenaire | IPartenaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartenaireService);
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

    it('should create a Partenaire', () => {
      const partenaire = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partenaire).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Partenaire', () => {
      const partenaire = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partenaire).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Partenaire', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Partenaire', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Partenaire', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartenaireToCollectionIfMissing', () => {
      it('should add a Partenaire to an empty array', () => {
        const partenaire: IPartenaire = sampleWithRequiredData;
        expectedResult = service.addPartenaireToCollectionIfMissing([], partenaire);
        expect(expectedResult).toEqual([partenaire]);
      });

      it('should not add a Partenaire to an array that contains it', () => {
        const partenaire: IPartenaire = sampleWithRequiredData;
        const partenaireCollection: IPartenaire[] = [
          {
            ...partenaire,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartenaireToCollectionIfMissing(partenaireCollection, partenaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Partenaire to an array that doesn't contain it", () => {
        const partenaire: IPartenaire = sampleWithRequiredData;
        const partenaireCollection: IPartenaire[] = [sampleWithPartialData];
        expectedResult = service.addPartenaireToCollectionIfMissing(partenaireCollection, partenaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partenaire);
      });

      it('should add only unique Partenaire to an array', () => {
        const partenaireArray: IPartenaire[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partenaireCollection: IPartenaire[] = [sampleWithRequiredData];
        expectedResult = service.addPartenaireToCollectionIfMissing(partenaireCollection, ...partenaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partenaire: IPartenaire = sampleWithRequiredData;
        const partenaire2: IPartenaire = sampleWithPartialData;
        expectedResult = service.addPartenaireToCollectionIfMissing([], partenaire, partenaire2);
        expect(expectedResult).toEqual([partenaire, partenaire2]);
      });

      it('should accept null and undefined values', () => {
        const partenaire: IPartenaire = sampleWithRequiredData;
        expectedResult = service.addPartenaireToCollectionIfMissing([], null, partenaire, undefined);
        expect(expectedResult).toEqual([partenaire]);
      });

      it('should return initial array if no Partenaire is added', () => {
        const partenaireCollection: IPartenaire[] = [sampleWithRequiredData];
        expectedResult = service.addPartenaireToCollectionIfMissing(partenaireCollection, undefined, null);
        expect(expectedResult).toEqual(partenaireCollection);
      });
    });

    describe('comparePartenaire', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartenaire(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
        const entity2 = null;

        const compareResult1 = service.comparePartenaire(entity1, entity2);
        const compareResult2 = service.comparePartenaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
        const entity2 = { id: 'b6fc1546-6825-402d-9e00-2c8ab08f6246' };

        const compareResult1 = service.comparePartenaire(entity1, entity2);
        const compareResult2 = service.comparePartenaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
        const entity2 = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };

        const compareResult1 = service.comparePartenaire(entity1, entity2);
        const compareResult2 = service.comparePartenaire(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
