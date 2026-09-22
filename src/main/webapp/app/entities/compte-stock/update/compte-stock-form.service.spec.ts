import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../compte-stock.test-samples';

import { CompteStockFormService } from './compte-stock-form.service';

describe('CompteStock Form Service', () => {
  let service: CompteStockFormService;

  beforeEach(() => {
    service = TestBed.inject(CompteStockFormService);
  });

  describe('Service methods', () => {
    describe('createCompteStockFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompteStockFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            solde: expect.any(Object),
            service: expect.any(Object),
          }),
        );
      });

      it('passing ICompteStock should create a new form with FormGroup', () => {
        const formGroup = service.createCompteStockFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            solde: expect.any(Object),
            service: expect.any(Object),
          }),
        );
      });
    });

    describe('getCompteStock', () => {
      it('should return NewCompteStock for default CompteStock initial value', () => {
        const formGroup = service.createCompteStockFormGroup(sampleWithNewData);

        const compteStock = service.getCompteStock(formGroup);

        expect(compteStock).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompteStock for empty CompteStock initial value', () => {
        const formGroup = service.createCompteStockFormGroup();

        const compteStock = service.getCompteStock(formGroup);

        expect(compteStock).toMatchObject({});
      });

      it('should return ICompteStock', () => {
        const formGroup = service.createCompteStockFormGroup(sampleWithRequiredData);

        const compteStock = service.getCompteStock(formGroup);

        expect(compteStock).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompteStock should not enable id FormControl', () => {
        const formGroup = service.createCompteStockFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompteStock should disable id FormControl', () => {
        const formGroup = service.createCompteStockFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
