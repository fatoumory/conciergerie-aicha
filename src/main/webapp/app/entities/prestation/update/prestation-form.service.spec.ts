import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../prestation.test-samples';

import { PrestationFormService } from './prestation-form.service';

describe('Prestation Form Service', () => {
  let service: PrestationFormService;

  beforeEach(() => {
    service = TestBed.inject(PrestationFormService);
  });

  describe('Service methods', () => {
    describe('createPrestationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrestationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            demande: expect.any(Object),
            partenaire: expect.any(Object),
          }),
        );
      });

      it('passing IPrestation should create a new form with FormGroup', () => {
        const formGroup = service.createPrestationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            demande: expect.any(Object),
            partenaire: expect.any(Object),
          }),
        );
      });
    });

    describe('getPrestation', () => {
      it('should return NewPrestation for default Prestation initial value', () => {
        const formGroup = service.createPrestationFormGroup(sampleWithNewData);

        const prestation = service.getPrestation(formGroup);

        expect(prestation).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrestation for empty Prestation initial value', () => {
        const formGroup = service.createPrestationFormGroup();

        const prestation = service.getPrestation(formGroup);

        expect(prestation).toMatchObject({});
      });

      it('should return IPrestation', () => {
        const formGroup = service.createPrestationFormGroup(sampleWithRequiredData);

        const prestation = service.getPrestation(formGroup);

        expect(prestation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrestation should not enable id FormControl', () => {
        const formGroup = service.createPrestationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrestation should disable id FormControl', () => {
        const formGroup = service.createPrestationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
