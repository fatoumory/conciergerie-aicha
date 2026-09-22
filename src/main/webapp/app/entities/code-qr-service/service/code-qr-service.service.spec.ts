import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICodeQrService } from '../code-qr-service.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../code-qr-service.test-samples';

import { CodeQrServiceService, RestCodeQrService } from './code-qr-service.service';

const requireRestSample: RestCodeQrService = {
  ...sampleWithRequiredData,
  dateGeneration: sampleWithRequiredData.dateGeneration?.toJSON(),
  dateExpiration: sampleWithRequiredData.dateExpiration?.toJSON(),
};

describe('CodeQrService Service', () => {
  let service: CodeQrServiceService;
  let httpMock: HttpTestingController;
  let expectedResult: ICodeQrService | ICodeQrService[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CodeQrServiceService);
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

    it('should create a CodeQrService', () => {
      const codeQrService = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(codeQrService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CodeQrService', () => {
      const codeQrService = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(codeQrService).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CodeQrService', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CodeQrService', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CodeQrService', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCodeQrServiceToCollectionIfMissing', () => {
      it('should add a CodeQrService to an empty array', () => {
        const codeQrService: ICodeQrService = sampleWithRequiredData;
        expectedResult = service.addCodeQrServiceToCollectionIfMissing([], codeQrService);
        expect(expectedResult).toEqual([codeQrService]);
      });

      it('should not add a CodeQrService to an array that contains it', () => {
        const codeQrService: ICodeQrService = sampleWithRequiredData;
        const codeQrServiceCollection: ICodeQrService[] = [
          {
            ...codeQrService,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCodeQrServiceToCollectionIfMissing(codeQrServiceCollection, codeQrService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CodeQrService to an array that doesn't contain it", () => {
        const codeQrService: ICodeQrService = sampleWithRequiredData;
        const codeQrServiceCollection: ICodeQrService[] = [sampleWithPartialData];
        expectedResult = service.addCodeQrServiceToCollectionIfMissing(codeQrServiceCollection, codeQrService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeQrService);
      });

      it('should add only unique CodeQrService to an array', () => {
        const codeQrServiceArray: ICodeQrService[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const codeQrServiceCollection: ICodeQrService[] = [sampleWithRequiredData];
        expectedResult = service.addCodeQrServiceToCollectionIfMissing(codeQrServiceCollection, ...codeQrServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codeQrService: ICodeQrService = sampleWithRequiredData;
        const codeQrService2: ICodeQrService = sampleWithPartialData;
        expectedResult = service.addCodeQrServiceToCollectionIfMissing([], codeQrService, codeQrService2);
        expect(expectedResult).toEqual([codeQrService, codeQrService2]);
      });

      it('should accept null and undefined values', () => {
        const codeQrService: ICodeQrService = sampleWithRequiredData;
        expectedResult = service.addCodeQrServiceToCollectionIfMissing([], null, codeQrService, undefined);
        expect(expectedResult).toEqual([codeQrService]);
      });

      it('should return initial array if no CodeQrService is added', () => {
        const codeQrServiceCollection: ICodeQrService[] = [sampleWithRequiredData];
        expectedResult = service.addCodeQrServiceToCollectionIfMissing(codeQrServiceCollection, undefined, null);
        expect(expectedResult).toEqual(codeQrServiceCollection);
      });
    });

    describe('compareCodeQrService', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCodeQrService(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
        const entity2 = null;

        const compareResult1 = service.compareCodeQrService(entity1, entity2);
        const compareResult2 = service.compareCodeQrService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
        const entity2 = { id: 'd244b8ab-9fc3-43a7-88ad-33f0a5709573' };

        const compareResult1 = service.compareCodeQrService(entity1, entity2);
        const compareResult2 = service.compareCodeQrService(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
        const entity2 = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };

        const compareResult1 = service.compareCodeQrService(entity1, entity2);
        const compareResult2 = service.compareCodeQrService(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
