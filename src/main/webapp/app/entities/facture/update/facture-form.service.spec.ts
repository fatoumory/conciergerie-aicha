import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facture.test-samples';

import { FactureFormService } from './facture-form.service';

describe('Facture Form Service', () => {
  let service: FactureFormService;

  beforeEach(() => {
    service = TestBed.inject(FactureFormService);
  });

  describe('Service methods', () => {
    describe('createFactureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            montant: expect.any(Object),
            dateGeneration: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });

      it('passing IFacture should create a new form with FormGroup', () => {
        const formGroup = service.createFactureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            montant: expect.any(Object),
            dateGeneration: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacture', () => {
      it('should return NewFacture for default Facture initial value', () => {
        const formGroup = service.createFactureFormGroup(sampleWithNewData);

        const facture = service.getFacture(formGroup);

        expect(facture).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacture for empty Facture initial value', () => {
        const formGroup = service.createFactureFormGroup();

        const facture = service.getFacture(formGroup);

        expect(facture).toMatchObject({});
      });

      it('should return IFacture', () => {
        const formGroup = service.createFactureFormGroup(sampleWithRequiredData);

        const facture = service.getFacture(formGroup);

        expect(facture).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacture should not enable id FormControl', () => {
        const formGroup = service.createFactureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacture should disable id FormControl', () => {
        const formGroup = service.createFactureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
