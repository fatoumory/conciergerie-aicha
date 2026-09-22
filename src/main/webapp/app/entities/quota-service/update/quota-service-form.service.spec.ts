import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quota-service.test-samples';

import { QuotaServiceFormService } from './quota-service-form.service';

describe('QuotaService Form Service', () => {
  let service: QuotaServiceFormService;

  beforeEach(() => {
    service = TestBed.inject(QuotaServiceFormService);
  });

  describe('Service methods', () => {
    describe('createQuotaServiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuotaServiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite: expect.any(Object),
            unite: expect.any(Object),
            periode: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            eligibiliteService: expect.any(Object),
          }),
        );
      });

      it('passing IQuotaService should create a new form with FormGroup', () => {
        const formGroup = service.createQuotaServiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite: expect.any(Object),
            unite: expect.any(Object),
            periode: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            eligibiliteService: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuotaService', () => {
      it('should return NewQuotaService for default QuotaService initial value', () => {
        const formGroup = service.createQuotaServiceFormGroup(sampleWithNewData);

        const quotaService = service.getQuotaService(formGroup);

        expect(quotaService).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuotaService for empty QuotaService initial value', () => {
        const formGroup = service.createQuotaServiceFormGroup();

        const quotaService = service.getQuotaService(formGroup);

        expect(quotaService).toMatchObject({});
      });

      it('should return IQuotaService', () => {
        const formGroup = service.createQuotaServiceFormGroup(sampleWithRequiredData);

        const quotaService = service.getQuotaService(formGroup);

        expect(quotaService).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuotaService should not enable id FormControl', () => {
        const formGroup = service.createQuotaServiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuotaService should disable id FormControl', () => {
        const formGroup = service.createQuotaServiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
