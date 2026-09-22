import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartenaireZone } from '../partenaire-zone.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../partenaire-zone.test-samples';

import { PartenaireZoneService } from './partenaire-zone.service';

const requireRestSample: IPartenaireZone = {
  ...sampleWithRequiredData,
};

describe('PartenaireZone Service', () => {
  let service: PartenaireZoneService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartenaireZone | IPartenaireZone[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartenaireZoneService);
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

    it('should create a PartenaireZone', () => {
      const partenaireZone = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partenaireZone).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartenaireZone', () => {
      const partenaireZone = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partenaireZone).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartenaireZone', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartenaireZone', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartenaireZone', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartenaireZoneToCollectionIfMissing', () => {
      it('should add a PartenaireZone to an empty array', () => {
        const partenaireZone: IPartenaireZone = sampleWithRequiredData;
        expectedResult = service.addPartenaireZoneToCollectionIfMissing([], partenaireZone);
        expect(expectedResult).toEqual([partenaireZone]);
      });

      it('should not add a PartenaireZone to an array that contains it', () => {
        const partenaireZone: IPartenaireZone = sampleWithRequiredData;
        const partenaireZoneCollection: IPartenaireZone[] = [
          {
            ...partenaireZone,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartenaireZoneToCollectionIfMissing(partenaireZoneCollection, partenaireZone);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartenaireZone to an array that doesn't contain it", () => {
        const partenaireZone: IPartenaireZone = sampleWithRequiredData;
        const partenaireZoneCollection: IPartenaireZone[] = [sampleWithPartialData];
        expectedResult = service.addPartenaireZoneToCollectionIfMissing(partenaireZoneCollection, partenaireZone);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partenaireZone);
      });

      it('should add only unique PartenaireZone to an array', () => {
        const partenaireZoneArray: IPartenaireZone[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partenaireZoneCollection: IPartenaireZone[] = [sampleWithRequiredData];
        expectedResult = service.addPartenaireZoneToCollectionIfMissing(partenaireZoneCollection, ...partenaireZoneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partenaireZone: IPartenaireZone = sampleWithRequiredData;
        const partenaireZone2: IPartenaireZone = sampleWithPartialData;
        expectedResult = service.addPartenaireZoneToCollectionIfMissing([], partenaireZone, partenaireZone2);
        expect(expectedResult).toEqual([partenaireZone, partenaireZone2]);
      });

      it('should accept null and undefined values', () => {
        const partenaireZone: IPartenaireZone = sampleWithRequiredData;
        expectedResult = service.addPartenaireZoneToCollectionIfMissing([], null, partenaireZone, undefined);
        expect(expectedResult).toEqual([partenaireZone]);
      });

      it('should return initial array if no PartenaireZone is added', () => {
        const partenaireZoneCollection: IPartenaireZone[] = [sampleWithRequiredData];
        expectedResult = service.addPartenaireZoneToCollectionIfMissing(partenaireZoneCollection, undefined, null);
        expect(expectedResult).toEqual(partenaireZoneCollection);
      });
    });

    describe('comparePartenaireZone', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartenaireZone(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' };
        const entity2 = null;

        const compareResult1 = service.comparePartenaireZone(entity1, entity2);
        const compareResult2 = service.comparePartenaireZone(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' };
        const entity2 = { id: '2f4bd363-1ad5-45e4-9e1d-da9594acf205' };

        const compareResult1 = service.comparePartenaireZone(entity1, entity2);
        const compareResult2 = service.comparePartenaireZone(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' };
        const entity2 = { id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' };

        const compareResult1 = service.comparePartenaireZone(entity1, entity2);
        const compareResult2 = service.comparePartenaireZone(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
