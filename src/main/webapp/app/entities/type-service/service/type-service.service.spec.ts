import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITypeService } from '../type-service.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../type-service.test-samples';

import { TypeServiceService } from './type-service.service';

const requireRestSample: ITypeService = {
  ...sampleWithRequiredData,
};

describe('TypeService Service', () => {
  let service: TypeServiceService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeService | ITypeService[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TypeServiceService);
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

    it('should create a TypeService', () => {
      const typeService = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeService', () => {
      const typeService = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeService', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeService', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeService', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTypeServiceToCollectionIfMissing', () => {
      it('should add a TypeService to an empty array', () => {
        const typeService: ITypeService = sampleWithRequiredData;
        expectedResult = service.addTypeServiceToCollectionIfMissing([], typeService);
        expect(expectedResult).toEqual([typeService]);
      });

      it('should not add a TypeService to an array that contains it', () => {
        const typeService: ITypeService = sampleWithRequiredData;
        const typeServiceCollection: ITypeService[] = [
          {
            ...typeService,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeServiceToCollectionIfMissing(typeServiceCollection, typeService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeService to an array that doesn't contain it", () => {
        const typeService: ITypeService = sampleWithRequiredData;
        const typeServiceCollection: ITypeService[] = [sampleWithPartialData];
        expectedResult = service.addTypeServiceToCollectionIfMissing(typeServiceCollection, typeService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeService);
      });

      it('should add only unique TypeService to an array', () => {
        const typeServiceArray: ITypeService[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeServiceCollection: ITypeService[] = [sampleWithRequiredData];
        expectedResult = service.addTypeServiceToCollectionIfMissing(typeServiceCollection, ...typeServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeService: ITypeService = sampleWithRequiredData;
        const typeService2: ITypeService = sampleWithPartialData;
        expectedResult = service.addTypeServiceToCollectionIfMissing([], typeService, typeService2);
        expect(expectedResult).toEqual([typeService, typeService2]);
      });

      it('should accept null and undefined values', () => {
        const typeService: ITypeService = sampleWithRequiredData;
        expectedResult = service.addTypeServiceToCollectionIfMissing([], null, typeService, undefined);
        expect(expectedResult).toEqual([typeService]);
      });

      it('should return initial array if no TypeService is added', () => {
        const typeServiceCollection: ITypeService[] = [sampleWithRequiredData];
        expectedResult = service.addTypeServiceToCollectionIfMissing(typeServiceCollection, undefined, null);
        expect(expectedResult).toEqual(typeServiceCollection);
      });
    });

    describe('compareTypeService', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeService(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
        const entity2 = null;

        const compareResult1 = service.compareTypeService(entity1, entity2);
        const compareResult2 = service.compareTypeService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
        const entity2 = { id: 'ca7ba5b9-b039-4856-8f31-aa9428ca24bc' };

        const compareResult1 = service.compareTypeService(entity1, entity2);
        const compareResult2 = service.compareTypeService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
        const entity2 = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };

        const compareResult1 = service.compareTypeService(entity1, entity2);
        const compareResult2 = service.compareTypeService(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
