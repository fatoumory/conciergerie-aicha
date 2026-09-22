import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../utilisation-code-qr.test-samples';

import { UtilisationCodeQrFormService } from './utilisation-code-qr-form.service';

describe('UtilisationCodeQr Form Service', () => {
  let service: UtilisationCodeQrFormService;

  beforeEach(() => {
    service = TestBed.inject(UtilisationCodeQrFormService);
  });

  describe('Service methods', () => {
    describe('createUtilisationCodeQrFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUtilisationCodeQrFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateUtilisation: expect.any(Object),
            codeQrService: expect.any(Object),
            partenaire: expect.any(Object),
          }),
        );
      });

      it('passing IUtilisationCodeQr should create a new form with FormGroup', () => {
        const formGroup = service.createUtilisationCodeQrFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateUtilisation: expect.any(Object),
            codeQrService: expect.any(Object),
            partenaire: expect.any(Object),
          }),
        );
      });
    });

    describe('getUtilisationCodeQr', () => {
      it('should return NewUtilisationCodeQr for default UtilisationCodeQr initial value', () => {
        const formGroup = service.createUtilisationCodeQrFormGroup(sampleWithNewData);

        const utilisationCodeQr = service.getUtilisationCodeQr(formGroup);

        expect(utilisationCodeQr).toMatchObject(sampleWithNewData);
      });

      it('should return NewUtilisationCodeQr for empty UtilisationCodeQr initial value', () => {
        const formGroup = service.createUtilisationCodeQrFormGroup();

        const utilisationCodeQr = service.getUtilisationCodeQr(formGroup);

        expect(utilisationCodeQr).toMatchObject({});
      });

      it('should return IUtilisationCodeQr', () => {
        const formGroup = service.createUtilisationCodeQrFormGroup(sampleWithRequiredData);

        const utilisationCodeQr = service.getUtilisationCodeQr(formGroup);

        expect(utilisationCodeQr).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUtilisationCodeQr should not enable id FormControl', () => {
        const formGroup = service.createUtilisationCodeQrFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUtilisationCodeQr should disable id FormControl', () => {
        const formGroup = service.createUtilisationCodeQrFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
