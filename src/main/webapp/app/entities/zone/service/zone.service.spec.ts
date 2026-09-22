import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IZone } from '../zone.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../zone.test-samples';

import { ZoneService } from './zone.service';

const requireRestSample: IZone = {
  ...sampleWithRequiredData,
};

describe('Zone Service', () => {
  let service: ZoneService;
  let httpMock: HttpTestingController;
  let expectedResult: IZone | IZone[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ZoneService);
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

    it('should create a Zone', () => {
      const zone = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(zone).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Zone', () => {
      const zone = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(zone).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Zone', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Zone', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Zone', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addZoneToCollectionIfMissing', () => {
      it('should add a Zone to an empty array', () => {
        const zone: IZone = sampleWithRequiredData;
        expectedResult = service.addZoneToCollectionIfMissing([], zone);
        expect(expectedResult).toEqual([zone]);
      });

      it('should not add a Zone to an array that contains it', () => {
        const zone: IZone = sampleWithRequiredData;
        const zoneCollection: IZone[] = [
          {
            ...zone,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, zone);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Zone to an array that doesn't contain it", () => {
        const zone: IZone = sampleWithRequiredData;
        const zoneCollection: IZone[] = [sampleWithPartialData];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, zone);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zone);
      });

      it('should add only unique Zone to an array', () => {
        const zoneArray: IZone[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const zoneCollection: IZone[] = [sampleWithRequiredData];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, ...zoneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const zone: IZone = sampleWithRequiredData;
        const zone2: IZone = sampleWithPartialData;
        expectedResult = service.addZoneToCollectionIfMissing([], zone, zone2);
        expect(expectedResult).toEqual([zone, zone2]);
      });

      it('should accept null and undefined values', () => {
        const zone: IZone = sampleWithRequiredData;
        expectedResult = service.addZoneToCollectionIfMissing([], null, zone, undefined);
        expect(expectedResult).toEqual([zone]);
      });

      it('should return initial array if no Zone is added', () => {
        const zoneCollection: IZone[] = [sampleWithRequiredData];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, undefined, null);
        expect(expectedResult).toEqual(zoneCollection);
      });
    });

    describe('compareZone', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareZone(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
        const entity2 = null;

        const compareResult1 = service.compareZone(entity1, entity2);
        const compareResult2 = service.compareZone(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
        const entity2 = { id: '49d119f6-ada1-4ddf-abe4-39f8231db1c3' };

        const compareResult1 = service.compareZone(entity1, entity2);
        const compareResult2 = service.compareZone(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
        const entity2 = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };

        const compareResult1 = service.compareZone(entity1, entity2);
        const compareResult2 = service.compareZone(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
