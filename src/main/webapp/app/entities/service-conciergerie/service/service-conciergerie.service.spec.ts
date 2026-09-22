import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IServiceConciergerie } from '../service-conciergerie.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../service-conciergerie.test-samples';

import { ServiceConciergerieService } from './service-conciergerie.service';

const requireRestSample: IServiceConciergerie = {
  ...sampleWithRequiredData,
};

describe('ServiceConciergerie Service', () => {
  let service: ServiceConciergerieService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceConciergerie | IServiceConciergerie[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceConciergerieService);
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

    it('should create a ServiceConciergerie', () => {
      const serviceConciergerie = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceConciergerie).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceConciergerie', () => {
      const serviceConciergerie = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceConciergerie).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceConciergerie', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceConciergerie', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceConciergerie', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addServiceConciergerieToCollectionIfMissing', () => {
      it('should add a ServiceConciergerie to an empty array', () => {
        const serviceConciergerie: IServiceConciergerie = sampleWithRequiredData;
        expectedResult = service.addServiceConciergerieToCollectionIfMissing([], serviceConciergerie);
        expect(expectedResult).toEqual([serviceConciergerie]);
      });

      it('should not add a ServiceConciergerie to an array that contains it', () => {
        const serviceConciergerie: IServiceConciergerie = sampleWithRequiredData;
        const serviceConciergerieCollection: IServiceConciergerie[] = [
          {
            ...serviceConciergerie,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceConciergerieToCollectionIfMissing(serviceConciergerieCollection, serviceConciergerie);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceConciergerie to an array that doesn't contain it", () => {
        const serviceConciergerie: IServiceConciergerie = sampleWithRequiredData;
        const serviceConciergerieCollection: IServiceConciergerie[] = [sampleWithPartialData];
        expectedResult = service.addServiceConciergerieToCollectionIfMissing(serviceConciergerieCollection, serviceConciergerie);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceConciergerie);
      });

      it('should add only unique ServiceConciergerie to an array', () => {
        const serviceConciergerieArray: IServiceConciergerie[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceConciergerieCollection: IServiceConciergerie[] = [sampleWithRequiredData];
        expectedResult = service.addServiceConciergerieToCollectionIfMissing(serviceConciergerieCollection, ...serviceConciergerieArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceConciergerie: IServiceConciergerie = sampleWithRequiredData;
        const serviceConciergerie2: IServiceConciergerie = sampleWithPartialData;
        expectedResult = service.addServiceConciergerieToCollectionIfMissing([], serviceConciergerie, serviceConciergerie2);
        expect(expectedResult).toEqual([serviceConciergerie, serviceConciergerie2]);
      });

      it('should accept null and undefined values', () => {
        const serviceConciergerie: IServiceConciergerie = sampleWithRequiredData;
        expectedResult = service.addServiceConciergerieToCollectionIfMissing([], null, serviceConciergerie, undefined);
        expect(expectedResult).toEqual([serviceConciergerie]);
      });

      it('should return initial array if no ServiceConciergerie is added', () => {
        const serviceConciergerieCollection: IServiceConciergerie[] = [sampleWithRequiredData];
        expectedResult = service.addServiceConciergerieToCollectionIfMissing(serviceConciergerieCollection, undefined, null);
        expect(expectedResult).toEqual(serviceConciergerieCollection);
      });
    });

    describe('compareServiceConciergerie', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceConciergerie(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
        const entity2 = null;

        const compareResult1 = service.compareServiceConciergerie(entity1, entity2);
        const compareResult2 = service.compareServiceConciergerie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
        const entity2 = { id: 'c043c152-745c-4486-b5a8-711daf859c96' };

        const compareResult1 = service.compareServiceConciergerie(entity1, entity2);
        const compareResult2 = service.compareServiceConciergerie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
        const entity2 = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };

        const compareResult1 = service.compareServiceConciergerie(entity1, entity2);
        const compareResult2 = service.compareServiceConciergerie(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
