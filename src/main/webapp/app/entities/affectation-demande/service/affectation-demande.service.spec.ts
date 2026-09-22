import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IAffectationDemande } from '../affectation-demande.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../affectation-demande.test-samples';

import { AffectationDemandeService, RestAffectationDemande } from './affectation-demande.service';

const requireRestSample: RestAffectationDemande = {
  ...sampleWithRequiredData,
  dateAffectation: sampleWithRequiredData.dateAffectation?.toJSON(),
};

describe('AffectationDemande Service', () => {
  let service: AffectationDemandeService;
  let httpMock: HttpTestingController;
  let expectedResult: IAffectationDemande | IAffectationDemande[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AffectationDemandeService);
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

    it('should create a AffectationDemande', () => {
      const affectationDemande = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(affectationDemande).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AffectationDemande', () => {
      const affectationDemande = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(affectationDemande).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AffectationDemande', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AffectationDemande', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AffectationDemande', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addAffectationDemandeToCollectionIfMissing', () => {
      it('should add a AffectationDemande to an empty array', () => {
        const affectationDemande: IAffectationDemande = sampleWithRequiredData;
        expectedResult = service.addAffectationDemandeToCollectionIfMissing([], affectationDemande);
        expect(expectedResult).toEqual([affectationDemande]);
      });

      it('should not add a AffectationDemande to an array that contains it', () => {
        const affectationDemande: IAffectationDemande = sampleWithRequiredData;
        const affectationDemandeCollection: IAffectationDemande[] = [
          {
            ...affectationDemande,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAffectationDemandeToCollectionIfMissing(affectationDemandeCollection, affectationDemande);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AffectationDemande to an array that doesn't contain it", () => {
        const affectationDemande: IAffectationDemande = sampleWithRequiredData;
        const affectationDemandeCollection: IAffectationDemande[] = [sampleWithPartialData];
        expectedResult = service.addAffectationDemandeToCollectionIfMissing(affectationDemandeCollection, affectationDemande);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(affectationDemande);
      });

      it('should add only unique AffectationDemande to an array', () => {
        const affectationDemandeArray: IAffectationDemande[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const affectationDemandeCollection: IAffectationDemande[] = [sampleWithRequiredData];
        expectedResult = service.addAffectationDemandeToCollectionIfMissing(affectationDemandeCollection, ...affectationDemandeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const affectationDemande: IAffectationDemande = sampleWithRequiredData;
        const affectationDemande2: IAffectationDemande = sampleWithPartialData;
        expectedResult = service.addAffectationDemandeToCollectionIfMissing([], affectationDemande, affectationDemande2);
        expect(expectedResult).toEqual([affectationDemande, affectationDemande2]);
      });

      it('should accept null and undefined values', () => {
        const affectationDemande: IAffectationDemande = sampleWithRequiredData;
        expectedResult = service.addAffectationDemandeToCollectionIfMissing([], null, affectationDemande, undefined);
        expect(expectedResult).toEqual([affectationDemande]);
      });

      it('should return initial array if no AffectationDemande is added', () => {
        const affectationDemandeCollection: IAffectationDemande[] = [sampleWithRequiredData];
        expectedResult = service.addAffectationDemandeToCollectionIfMissing(affectationDemandeCollection, undefined, null);
        expect(expectedResult).toEqual(affectationDemandeCollection);
      });
    });

    describe('compareAffectationDemande', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAffectationDemande(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' };
        const entity2 = null;

        const compareResult1 = service.compareAffectationDemande(entity1, entity2);
        const compareResult2 = service.compareAffectationDemande(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' };
        const entity2 = { id: '01609fa9-9d4c-41c5-974f-1dc953bb53c0' };

        const compareResult1 = service.compareAffectationDemande(entity1, entity2);
        const compareResult2 = service.compareAffectationDemande(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' };
        const entity2 = { id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' };

        const compareResult1 = service.compareAffectationDemande(entity1, entity2);
        const compareResult2 = service.compareAffectationDemande(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
