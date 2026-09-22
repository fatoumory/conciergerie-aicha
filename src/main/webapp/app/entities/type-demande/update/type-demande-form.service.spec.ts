import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../type-demande.test-samples';

import { TypeDemandeFormService } from './type-demande-form.service';

describe('TypeDemande Form Service', () => {
  let service: TypeDemandeFormService;

  beforeEach(() => {
    service = TestBed.inject(TypeDemandeFormService);
  });

  describe('Service methods', () => {
    describe('createTypeDemandeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeDemandeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing ITypeDemande should create a new form with FormGroup', () => {
        const formGroup = service.createTypeDemandeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeDemande', () => {
      it('should return NewTypeDemande for default TypeDemande initial value', () => {
        const formGroup = service.createTypeDemandeFormGroup(sampleWithNewData);

        const typeDemande = service.getTypeDemande(formGroup);

        expect(typeDemande).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeDemande for empty TypeDemande initial value', () => {
        const formGroup = service.createTypeDemandeFormGroup();

        const typeDemande = service.getTypeDemande(formGroup);

        expect(typeDemande).toMatchObject({});
      });

      it('should return ITypeDemande', () => {
        const formGroup = service.createTypeDemandeFormGroup(sampleWithRequiredData);

        const typeDemande = service.getTypeDemande(formGroup);

        expect(typeDemande).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeDemande should not enable id FormControl', () => {
        const formGroup = service.createTypeDemandeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeDemande should disable id FormControl', () => {
        const formGroup = service.createTypeDemandeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
