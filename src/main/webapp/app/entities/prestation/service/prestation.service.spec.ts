import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPrestation } from '../prestation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../prestation.test-samples';

import { PrestationService, RestPrestation } from './prestation.service';

const requireRestSample: RestPrestation = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
  dateFin: sampleWithRequiredData.dateFin?.toJSON(),
};

describe('Prestation Service', () => {
  let service: PrestationService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrestation | IPrestation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PrestationService);
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

    it('should create a Prestation', () => {
      const prestation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prestation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Prestation', () => {
      const prestation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prestation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Prestation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Prestation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Prestation', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPrestationToCollectionIfMissing', () => {
      it('should add a Prestation to an empty array', () => {
        const prestation: IPrestation = sampleWithRequiredData;
        expectedResult = service.addPrestationToCollectionIfMissing([], prestation);
        expect(expectedResult).toEqual([prestation]);
      });

      it('should not add a Prestation to an array that contains it', () => {
        const prestation: IPrestation = sampleWithRequiredData;
        const prestationCollection: IPrestation[] = [
          {
            ...prestation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrestationToCollectionIfMissing(prestationCollection, prestation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Prestation to an array that doesn't contain it", () => {
        const prestation: IPrestation = sampleWithRequiredData;
        const prestationCollection: IPrestation[] = [sampleWithPartialData];
        expectedResult = service.addPrestationToCollectionIfMissing(prestationCollection, prestation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prestation);
      });

      it('should add only unique Prestation to an array', () => {
        const prestationArray: IPrestation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prestationCollection: IPrestation[] = [sampleWithRequiredData];
        expectedResult = service.addPrestationToCollectionIfMissing(prestationCollection, ...prestationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prestation: IPrestation = sampleWithRequiredData;
        const prestation2: IPrestation = sampleWithPartialData;
        expectedResult = service.addPrestationToCollectionIfMissing([], prestation, prestation2);
        expect(expectedResult).toEqual([prestation, prestation2]);
      });

      it('should accept null and undefined values', () => {
        const prestation: IPrestation = sampleWithRequiredData;
        expectedResult = service.addPrestationToCollectionIfMissing([], null, prestation, undefined);
        expect(expectedResult).toEqual([prestation]);
      });

      it('should return initial array if no Prestation is added', () => {
        const prestationCollection: IPrestation[] = [sampleWithRequiredData];
        expectedResult = service.addPrestationToCollectionIfMissing(prestationCollection, undefined, null);
        expect(expectedResult).toEqual(prestationCollection);
      });
    });

    describe('comparePrestation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrestation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' };
        const entity2 = null;

        const compareResult1 = service.comparePrestation(entity1, entity2);
        const compareResult2 = service.comparePrestation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' };
        const entity2 = { id: '53ada915-c0bd-45c2-9fc0-60bd5169f1bd' };

        const compareResult1 = service.comparePrestation(entity1, entity2);
        const compareResult2 = service.comparePrestation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' };
        const entity2 = { id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' };

        const compareResult1 = service.comparePrestation(entity1, entity2);
        const compareResult2 = service.comparePrestation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
