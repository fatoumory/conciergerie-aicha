import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../consommation-quota.test-samples';

import { ConsommationQuotaFormService } from './consommation-quota-form.service';

describe('ConsommationQuota Form Service', () => {
  let service: ConsommationQuotaFormService;

  beforeEach(() => {
    service = TestBed.inject(ConsommationQuotaFormService);
  });

  describe('Service methods', () => {
    describe('createConsommationQuotaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConsommationQuotaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantite: expect.any(Object),
            dateConsommation: expect.any(Object),
            client: expect.any(Object),
            quotaService: expect.any(Object),
            quotaDetail: expect.any(Object),
          }),
        );
      });

      it('passing IConsommationQuota should create a new form with FormGroup', () => {
        const formGroup = service.createConsommationQuotaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantite: expect.any(Object),
            dateConsommation: expect.any(Object),
            client: expect.any(Object),
            quotaService: expect.any(Object),
            quotaDetail: expect.any(Object),
          }),
        );
      });
    });

    describe('getConsommationQuota', () => {
      it('should return NewConsommationQuota for default ConsommationQuota initial value', () => {
        const formGroup = service.createConsommationQuotaFormGroup(sampleWithNewData);

        const consommationQuota = service.getConsommationQuota(formGroup);

        expect(consommationQuota).toMatchObject(sampleWithNewData);
      });

      it('should return NewConsommationQuota for empty ConsommationQuota initial value', () => {
        const formGroup = service.createConsommationQuotaFormGroup();

        const consommationQuota = service.getConsommationQuota(formGroup);

        expect(consommationQuota).toMatchObject({});
      });

      it('should return IConsommationQuota', () => {
        const formGroup = service.createConsommationQuotaFormGroup(sampleWithRequiredData);

        const consommationQuota = service.getConsommationQuota(formGroup);

        expect(consommationQuota).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConsommationQuota should not enable id FormControl', () => {
        const formGroup = service.createConsommationQuotaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConsommationQuota should disable id FormControl', () => {
        const formGroup = service.createConsommationQuotaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
