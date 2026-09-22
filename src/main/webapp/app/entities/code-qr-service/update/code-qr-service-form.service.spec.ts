import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../code-qr-service.test-samples';

import { CodeQrServiceFormService } from './code-qr-service-form.service';

describe('CodeQrService Form Service', () => {
  let service: CodeQrServiceFormService;

  beforeEach(() => {
    service = TestBed.inject(CodeQrServiceFormService);
  });

  describe('Service methods', () => {
    describe('createCodeQrServiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCodeQrServiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            qrCode: expect.any(Object),
            dateGeneration: expect.any(Object),
            dateExpiration: expect.any(Object),
            statut: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });

      it('passing ICodeQrService should create a new form with FormGroup', () => {
        const formGroup = service.createCodeQrServiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            qrCode: expect.any(Object),
            dateGeneration: expect.any(Object),
            dateExpiration: expect.any(Object),
            statut: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });
    });

    describe('getCodeQrService', () => {
      it('should return NewCodeQrService for default CodeQrService initial value', () => {
        const formGroup = service.createCodeQrServiceFormGroup(sampleWithNewData);

        const codeQrService = service.getCodeQrService(formGroup);

        expect(codeQrService).toMatchObject(sampleWithNewData);
      });

      it('should return NewCodeQrService for empty CodeQrService initial value', () => {
        const formGroup = service.createCodeQrServiceFormGroup();

        const codeQrService = service.getCodeQrService(formGroup);

        expect(codeQrService).toMatchObject({});
      });

      it('should return ICodeQrService', () => {
        const formGroup = service.createCodeQrServiceFormGroup(sampleWithRequiredData);

        const codeQrService = service.getCodeQrService(formGroup);

        expect(codeQrService).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICodeQrService should not enable id FormControl', () => {
        const formGroup = service.createCodeQrServiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCodeQrService should disable id FormControl', () => {
        const formGroup = service.createCodeQrServiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
