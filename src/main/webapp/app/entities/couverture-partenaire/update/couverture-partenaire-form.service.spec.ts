import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../couverture-partenaire.test-samples';

import { CouverturePartenaireFormService } from './couverture-partenaire-form.service';

describe('CouverturePartenaire Form Service', () => {
  let service: CouverturePartenaireFormService;

  beforeEach(() => {
    service = TestBed.inject(CouverturePartenaireFormService);
  });

  describe('Service methods', () => {
    describe('createCouverturePartenaireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCouverturePartenaireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            partenaire: expect.any(Object),
            service: expect.any(Object),
          }),
        );
      });

      it('passing ICouverturePartenaire should create a new form with FormGroup', () => {
        const formGroup = service.createCouverturePartenaireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            partenaire: expect.any(Object),
            service: expect.any(Object),
          }),
        );
      });
    });

    describe('getCouverturePartenaire', () => {
      it('should return NewCouverturePartenaire for default CouverturePartenaire initial value', () => {
        const formGroup = service.createCouverturePartenaireFormGroup(sampleWithNewData);

        const couverturePartenaire = service.getCouverturePartenaire(formGroup);

        expect(couverturePartenaire).toMatchObject(sampleWithNewData);
      });

      it('should return NewCouverturePartenaire for empty CouverturePartenaire initial value', () => {
        const formGroup = service.createCouverturePartenaireFormGroup();

        const couverturePartenaire = service.getCouverturePartenaire(formGroup);

        expect(couverturePartenaire).toMatchObject({});
      });

      it('should return ICouverturePartenaire', () => {
        const formGroup = service.createCouverturePartenaireFormGroup(sampleWithRequiredData);

        const couverturePartenaire = service.getCouverturePartenaire(formGroup);

        expect(couverturePartenaire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICouverturePartenaire should not enable id FormControl', () => {
        const formGroup = service.createCouverturePartenaireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCouverturePartenaire should disable id FormControl', () => {
        const formGroup = service.createCouverturePartenaireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
