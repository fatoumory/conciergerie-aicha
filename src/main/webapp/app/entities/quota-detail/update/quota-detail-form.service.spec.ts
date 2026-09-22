import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quota-detail.test-samples';

import { QuotaDetailFormService } from './quota-detail-form.service';

describe('QuotaDetail Form Service', () => {
  let service: QuotaDetailFormService;

  beforeEach(() => {
    service = TestBed.inject(QuotaDetailFormService);
  });

  describe('Service methods', () => {
    describe('createQuotaDetailFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuotaDetailFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite: expect.any(Object),
            quotaService: expect.any(Object),
            zone: expect.any(Object),
          }),
        );
      });

      it('passing IQuotaDetail should create a new form with FormGroup', () => {
        const formGroup = service.createQuotaDetailFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite: expect.any(Object),
            quotaService: expect.any(Object),
            zone: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuotaDetail', () => {
      it('should return NewQuotaDetail for default QuotaDetail initial value', () => {
        const formGroup = service.createQuotaDetailFormGroup(sampleWithNewData);

        const quotaDetail = service.getQuotaDetail(formGroup);

        expect(quotaDetail).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuotaDetail for empty QuotaDetail initial value', () => {
        const formGroup = service.createQuotaDetailFormGroup();

        const quotaDetail = service.getQuotaDetail(formGroup);

        expect(quotaDetail).toMatchObject({});
      });

      it('should return IQuotaDetail', () => {
        const formGroup = service.createQuotaDetailFormGroup(sampleWithRequiredData);

        const quotaDetail = service.getQuotaDetail(formGroup);

        expect(quotaDetail).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuotaDetail should not enable id FormControl', () => {
        const formGroup = service.createQuotaDetailFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuotaDetail should disable id FormControl', () => {
        const formGroup = service.createQuotaDetailFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
