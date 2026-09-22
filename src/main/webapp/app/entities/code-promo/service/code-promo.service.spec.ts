import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { ICodePromo } from '../code-promo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../code-promo.test-samples';

import { CodePromoService, RestCodePromo } from './code-promo.service';

const requireRestSample: RestCodePromo = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('CodePromo Service', () => {
  let service: CodePromoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICodePromo | ICodePromo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CodePromoService);
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

    it('should create a CodePromo', () => {
      const codePromo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(codePromo).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CodePromo', () => {
      const codePromo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(codePromo).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CodePromo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CodePromo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CodePromo', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCodePromoToCollectionIfMissing', () => {
      it('should add a CodePromo to an empty array', () => {
        const codePromo: ICodePromo = sampleWithRequiredData;
        expectedResult = service.addCodePromoToCollectionIfMissing([], codePromo);
        expect(expectedResult).toEqual([codePromo]);
      });

      it('should not add a CodePromo to an array that contains it', () => {
        const codePromo: ICodePromo = sampleWithRequiredData;
        const codePromoCollection: ICodePromo[] = [
          {
            ...codePromo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCodePromoToCollectionIfMissing(codePromoCollection, codePromo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CodePromo to an array that doesn't contain it", () => {
        const codePromo: ICodePromo = sampleWithRequiredData;
        const codePromoCollection: ICodePromo[] = [sampleWithPartialData];
        expectedResult = service.addCodePromoToCollectionIfMissing(codePromoCollection, codePromo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codePromo);
      });

      it('should add only unique CodePromo to an array', () => {
        const codePromoArray: ICodePromo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const codePromoCollection: ICodePromo[] = [sampleWithRequiredData];
        expectedResult = service.addCodePromoToCollectionIfMissing(codePromoCollection, ...codePromoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codePromo: ICodePromo = sampleWithRequiredData;
        const codePromo2: ICodePromo = sampleWithPartialData;
        expectedResult = service.addCodePromoToCollectionIfMissing([], codePromo, codePromo2);
        expect(expectedResult).toEqual([codePromo, codePromo2]);
      });

      it('should accept null and undefined values', () => {
        const codePromo: ICodePromo = sampleWithRequiredData;
        expectedResult = service.addCodePromoToCollectionIfMissing([], null, codePromo, undefined);
        expect(expectedResult).toEqual([codePromo]);
      });

      it('should return initial array if no CodePromo is added', () => {
        const codePromoCollection: ICodePromo[] = [sampleWithRequiredData];
        expectedResult = service.addCodePromoToCollectionIfMissing(codePromoCollection, undefined, null);
        expect(expectedResult).toEqual(codePromoCollection);
      });
    });

    describe('compareCodePromo', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCodePromo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
        const entity2 = null;

        const compareResult1 = service.compareCodePromo(entity1, entity2);
        const compareResult2 = service.compareCodePromo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
        const entity2 = { id: '2f309410-f594-41bc-8818-5fbfb8f44a29' };

        const compareResult1 = service.compareCodePromo(entity1, entity2);
        const compareResult2 = service.compareCodePromo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
        const entity2 = { id: 'b727eb78-e176-4142-8685-21300126e80d' };

        const compareResult1 = service.compareCodePromo(entity1, entity2);
        const compareResult2 = service.compareCodePromo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
