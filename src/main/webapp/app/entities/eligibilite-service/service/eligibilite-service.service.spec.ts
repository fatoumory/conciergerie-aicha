import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IEligibiliteService } from '../eligibilite-service.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../eligibilite-service.test-samples';

import { EligibiliteServiceService, RestEligibiliteService } from './eligibilite-service.service';

const requireRestSample: RestEligibiliteService = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('EligibiliteService Service', () => {
  let service: EligibiliteServiceService;
  let httpMock: HttpTestingController;
  let expectedResult: IEligibiliteService | IEligibiliteService[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EligibiliteServiceService);
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

    it('should create a EligibiliteService', () => {
      const eligibiliteService = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eligibiliteService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EligibiliteService', () => {
      const eligibiliteService = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eligibiliteService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EligibiliteService', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EligibiliteService', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EligibiliteService', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEligibiliteServiceToCollectionIfMissing', () => {
      it('should add a EligibiliteService to an empty array', () => {
        const eligibiliteService: IEligibiliteService = sampleWithRequiredData;
        expectedResult = service.addEligibiliteServiceToCollectionIfMissing([], eligibiliteService);
        expect(expectedResult).toEqual([eligibiliteService]);
      });

      it('should not add a EligibiliteService to an array that contains it', () => {
        const eligibiliteService: IEligibiliteService = sampleWithRequiredData;
        const eligibiliteServiceCollection: IEligibiliteService[] = [
          {
            ...eligibiliteService,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEligibiliteServiceToCollectionIfMissing(eligibiliteServiceCollection, eligibiliteService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EligibiliteService to an array that doesn't contain it", () => {
        const eligibiliteService: IEligibiliteService = sampleWithRequiredData;
        const eligibiliteServiceCollection: IEligibiliteService[] = [sampleWithPartialData];
        expectedResult = service.addEligibiliteServiceToCollectionIfMissing(eligibiliteServiceCollection, eligibiliteService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eligibiliteService);
      });

      it('should add only unique EligibiliteService to an array', () => {
        const eligibiliteServiceArray: IEligibiliteService[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eligibiliteServiceCollection: IEligibiliteService[] = [sampleWithRequiredData];
        expectedResult = service.addEligibiliteServiceToCollectionIfMissing(eligibiliteServiceCollection, ...eligibiliteServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eligibiliteService: IEligibiliteService = sampleWithRequiredData;
        const eligibiliteService2: IEligibiliteService = sampleWithPartialData;
        expectedResult = service.addEligibiliteServiceToCollectionIfMissing([], eligibiliteService, eligibiliteService2);
        expect(expectedResult).toEqual([eligibiliteService, eligibiliteService2]);
      });

      it('should accept null and undefined values', () => {
        const eligibiliteService: IEligibiliteService = sampleWithRequiredData;
        expectedResult = service.addEligibiliteServiceToCollectionIfMissing([], null, eligibiliteService, undefined);
        expect(expectedResult).toEqual([eligibiliteService]);
      });

      it('should return initial array if no EligibiliteService is added', () => {
        const eligibiliteServiceCollection: IEligibiliteService[] = [sampleWithRequiredData];
        expectedResult = service.addEligibiliteServiceToCollectionIfMissing(eligibiliteServiceCollection, undefined, null);
        expect(expectedResult).toEqual(eligibiliteServiceCollection);
      });
    });

    describe('compareEligibiliteService', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEligibiliteService(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
        const entity2 = null;

        const compareResult1 = service.compareEligibiliteService(entity1, entity2);
        const compareResult2 = service.compareEligibiliteService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
        const entity2 = { id: 'e70583f6-81ec-4ba0-bc9f-d0bbd7deed39' };

        const compareResult1 = service.compareEligibiliteService(entity1, entity2);
        const compareResult2 = service.compareEligibiliteService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
        const entity2 = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };

        const compareResult1 = service.compareEligibiliteService(entity1, entity2);
        const compareResult2 = service.compareEligibiliteService(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
