import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IUtilisationCodeQr } from '../utilisation-code-qr.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../utilisation-code-qr.test-samples';

import { RestUtilisationCodeQr, UtilisationCodeQrService } from './utilisation-code-qr.service';

const requireRestSample: RestUtilisationCodeQr = {
  ...sampleWithRequiredData,
  dateUtilisation: sampleWithRequiredData.dateUtilisation?.toJSON(),
};

describe('UtilisationCodeQr Service', () => {
  let service: UtilisationCodeQrService;
  let httpMock: HttpTestingController;
  let expectedResult: IUtilisationCodeQr | IUtilisationCodeQr[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UtilisationCodeQrService);
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

    it('should create a UtilisationCodeQr', () => {
      const utilisationCodeQr = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(utilisationCodeQr).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UtilisationCodeQr', () => {
      const utilisationCodeQr = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(utilisationCodeQr).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UtilisationCodeQr', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UtilisationCodeQr', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UtilisationCodeQr', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addUtilisationCodeQrToCollectionIfMissing', () => {
      it('should add a UtilisationCodeQr to an empty array', () => {
        const utilisationCodeQr: IUtilisationCodeQr = sampleWithRequiredData;
        expectedResult = service.addUtilisationCodeQrToCollectionIfMissing([], utilisationCodeQr);
        expect(expectedResult).toEqual([utilisationCodeQr]);
      });

      it('should not add a UtilisationCodeQr to an array that contains it', () => {
        const utilisationCodeQr: IUtilisationCodeQr = sampleWithRequiredData;
        const utilisationCodeQrCollection: IUtilisationCodeQr[] = [
          {
            ...utilisationCodeQr,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUtilisationCodeQrToCollectionIfMissing(utilisationCodeQrCollection, utilisationCodeQr);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UtilisationCodeQr to an array that doesn't contain it", () => {
        const utilisationCodeQr: IUtilisationCodeQr = sampleWithRequiredData;
        const utilisationCodeQrCollection: IUtilisationCodeQr[] = [sampleWithPartialData];
        expectedResult = service.addUtilisationCodeQrToCollectionIfMissing(utilisationCodeQrCollection, utilisationCodeQr);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utilisationCodeQr);
      });

      it('should add only unique UtilisationCodeQr to an array', () => {
        const utilisationCodeQrArray: IUtilisationCodeQr[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const utilisationCodeQrCollection: IUtilisationCodeQr[] = [sampleWithRequiredData];
        expectedResult = service.addUtilisationCodeQrToCollectionIfMissing(utilisationCodeQrCollection, ...utilisationCodeQrArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utilisationCodeQr: IUtilisationCodeQr = sampleWithRequiredData;
        const utilisationCodeQr2: IUtilisationCodeQr = sampleWithPartialData;
        expectedResult = service.addUtilisationCodeQrToCollectionIfMissing([], utilisationCodeQr, utilisationCodeQr2);
        expect(expectedResult).toEqual([utilisationCodeQr, utilisationCodeQr2]);
      });

      it('should accept null and undefined values', () => {
        const utilisationCodeQr: IUtilisationCodeQr = sampleWithRequiredData;
        expectedResult = service.addUtilisationCodeQrToCollectionIfMissing([], null, utilisationCodeQr, undefined);
        expect(expectedResult).toEqual([utilisationCodeQr]);
      });

      it('should return initial array if no UtilisationCodeQr is added', () => {
        const utilisationCodeQrCollection: IUtilisationCodeQr[] = [sampleWithRequiredData];
        expectedResult = service.addUtilisationCodeQrToCollectionIfMissing(utilisationCodeQrCollection, undefined, null);
        expect(expectedResult).toEqual(utilisationCodeQrCollection);
      });
    });

    describe('compareUtilisationCodeQr', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUtilisationCodeQr(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '9609ad2a-cecd-47c9-a80a-730574aa5903' };
        const entity2 = null;

        const compareResult1 = service.compareUtilisationCodeQr(entity1, entity2);
        const compareResult2 = service.compareUtilisationCodeQr(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '9609ad2a-cecd-47c9-a80a-730574aa5903' };
        const entity2 = { id: '6cbdc051-f287-4d20-85bd-683ae43a902a' };

        const compareResult1 = service.compareUtilisationCodeQr(entity1, entity2);
        const compareResult2 = service.compareUtilisationCodeQr(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: '9609ad2a-cecd-47c9-a80a-730574aa5903' };
        const entity2 = { id: '9609ad2a-cecd-47c9-a80a-730574aa5903' };

        const compareResult1 = service.compareUtilisationCodeQr(entity1, entity2);
        const compareResult2 = service.compareUtilisationCodeQr(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
