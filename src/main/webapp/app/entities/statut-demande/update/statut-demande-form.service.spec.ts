import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../statut-demande.test-samples';

import { StatutDemandeFormService } from './statut-demande-form.service';

describe('StatutDemande Form Service', () => {
  let service: StatutDemandeFormService;

  beforeEach(() => {
    service = TestBed.inject(StatutDemandeFormService);
  });

  describe('Service methods', () => {
    describe('createStatutDemandeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStatutDemandeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing IStatutDemande should create a new form with FormGroup', () => {
        const formGroup = service.createStatutDemandeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getStatutDemande', () => {
      it('should return NewStatutDemande for default StatutDemande initial value', () => {
        const formGroup = service.createStatutDemandeFormGroup(sampleWithNewData);

        const statutDemande = service.getStatutDemande(formGroup);

        expect(statutDemande).toMatchObject(sampleWithNewData);
      });

      it('should return NewStatutDemande for empty StatutDemande initial value', () => {
        const formGroup = service.createStatutDemandeFormGroup();

        const statutDemande = service.getStatutDemande(formGroup);

        expect(statutDemande).toMatchObject({});
      });

      it('should return IStatutDemande', () => {
        const formGroup = service.createStatutDemandeFormGroup(sampleWithRequiredData);

        const statutDemande = service.getStatutDemande(formGroup);

        expect(statutDemande).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStatutDemande should not enable id FormControl', () => {
        const formGroup = service.createStatutDemandeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStatutDemande should disable id FormControl', () => {
        const formGroup = service.createStatutDemandeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
