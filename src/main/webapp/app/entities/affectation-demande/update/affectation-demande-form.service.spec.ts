import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../affectation-demande.test-samples';

import { AffectationDemandeFormService } from './affectation-demande-form.service';

describe('AffectationDemande Form Service', () => {
  let service: AffectationDemandeFormService;

  beforeEach(() => {
    service = TestBed.inject(AffectationDemandeFormService);
  });

  describe('Service methods', () => {
    describe('createAffectationDemandeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAffectationDemandeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAffectation: expect.any(Object),
            demande: expect.any(Object),
            partenaire: expect.any(Object),
          }),
        );
      });

      it('passing IAffectationDemande should create a new form with FormGroup', () => {
        const formGroup = service.createAffectationDemandeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAffectation: expect.any(Object),
            demande: expect.any(Object),
            partenaire: expect.any(Object),
          }),
        );
      });
    });

    describe('getAffectationDemande', () => {
      it('should return NewAffectationDemande for default AffectationDemande initial value', () => {
        const formGroup = service.createAffectationDemandeFormGroup(sampleWithNewData);

        const affectationDemande = service.getAffectationDemande(formGroup);

        expect(affectationDemande).toMatchObject(sampleWithNewData);
      });

      it('should return NewAffectationDemande for empty AffectationDemande initial value', () => {
        const formGroup = service.createAffectationDemandeFormGroup();

        const affectationDemande = service.getAffectationDemande(formGroup);

        expect(affectationDemande).toMatchObject({});
      });

      it('should return IAffectationDemande', () => {
        const formGroup = service.createAffectationDemandeFormGroup(sampleWithRequiredData);

        const affectationDemande = service.getAffectationDemande(formGroup);

        expect(affectationDemande).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAffectationDemande should not enable id FormControl', () => {
        const formGroup = service.createAffectationDemandeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAffectationDemande should disable id FormControl', () => {
        const formGroup = service.createAffectationDemandeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
