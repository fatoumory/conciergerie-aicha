import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITypeDemande } from '../type-demande.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../type-demande.test-samples';

import { TypeDemandeService } from './type-demande.service';

const requireRestSample: ITypeDemande = {
  ...sampleWithRequiredData,
};

describe('TypeDemande Service', () => {
  let service: TypeDemandeService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeDemande | ITypeDemande[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TypeDemandeService);
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

    it('should create a TypeDemande', () => {
      const typeDemande = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeDemande).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeDemande', () => {
      const typeDemande = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeDemande).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeDemande', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeDemande', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeDemande', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTypeDemandeToCollectionIfMissing', () => {
      it('should add a TypeDemande to an empty array', () => {
        const typeDemande: ITypeDemande = sampleWithRequiredData;
        expectedResult = service.addTypeDemandeToCollectionIfMissing([], typeDemande);
        expect(expectedResult).toEqual([typeDemande]);
      });

      it('should not add a TypeDemande to an array that contains it', () => {
        const typeDemande: ITypeDemande = sampleWithRequiredData;
        const typeDemandeCollection: ITypeDemande[] = [
          {
            ...typeDemande,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeDemandeToCollectionIfMissing(typeDemandeCollection, typeDemande);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeDemande to an array that doesn't contain it", () => {
        const typeDemande: ITypeDemande = sampleWithRequiredData;
        const typeDemandeCollection: ITypeDemande[] = [sampleWithPartialData];
        expectedResult = service.addTypeDemandeToCollectionIfMissing(typeDemandeCollection, typeDemande);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeDemande);
      });

      it('should add only unique TypeDemande to an array', () => {
        const typeDemandeArray: ITypeDemande[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeDemandeCollection: ITypeDemande[] = [sampleWithRequiredData];
        expectedResult = service.addTypeDemandeToCollectionIfMissing(typeDemandeCollection, ...typeDemandeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeDemande: ITypeDemande = sampleWithRequiredData;
        const typeDemande2: ITypeDemande = sampleWithPartialData;
        expectedResult = service.addTypeDemandeToCollectionIfMissing([], typeDemande, typeDemande2);
        expect(expectedResult).toEqual([typeDemande, typeDemande2]);
      });

      it('should accept null and undefined values', () => {
        const typeDemande: ITypeDemande = sampleWithRequiredData;
        expectedResult = service.addTypeDemandeToCollectionIfMissing([], null, typeDemande, undefined);
        expect(expectedResult).toEqual([typeDemande]);
      });

      it('should return initial array if no TypeDemande is added', () => {
        const typeDemandeCollection: ITypeDemande[] = [sampleWithRequiredData];
        expectedResult = service.addTypeDemandeToCollectionIfMissing(typeDemandeCollection, undefined, null);
        expect(expectedResult).toEqual(typeDemandeCollection);
      });
    });

    describe('compareTypeDemande', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeDemande(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
        const entity2 = null;

        const compareResult1 = service.compareTypeDemande(entity1, entity2);
        const compareResult2 = service.compareTypeDemande(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
        const entity2 = { id: 'b3d6689f-dd1c-49f0-b65c-9fee5dabd02e' };

        const compareResult1 = service.compareTypeDemande(entity1, entity2);
        const compareResult2 = service.compareTypeDemande(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
        const entity2 = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };

        const compareResult1 = service.compareTypeDemande(entity1, entity2);
        const compareResult2 = service.compareTypeDemande(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
