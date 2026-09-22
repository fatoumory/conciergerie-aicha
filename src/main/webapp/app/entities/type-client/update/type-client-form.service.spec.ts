import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../type-client.test-samples';

import { TypeClientFormService } from './type-client-form.service';

describe('TypeClient Form Service', () => {
  let service: TypeClientFormService;

  beforeEach(() => {
    service = TestBed.inject(TypeClientFormService);
  });

  describe('Service methods', () => {
    describe('createTypeClientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeClientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing ITypeClient should create a new form with FormGroup', () => {
        const formGroup = service.createTypeClientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeClient', () => {
      it('should return NewTypeClient for default TypeClient initial value', () => {
        const formGroup = service.createTypeClientFormGroup(sampleWithNewData);

        const typeClient = service.getTypeClient(formGroup);

        expect(typeClient).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeClient for empty TypeClient initial value', () => {
        const formGroup = service.createTypeClientFormGroup();

        const typeClient = service.getTypeClient(formGroup);

        expect(typeClient).toMatchObject({});
      });

      it('should return ITypeClient', () => {
        const formGroup = service.createTypeClientFormGroup(sampleWithRequiredData);

        const typeClient = service.getTypeClient(formGroup);

        expect(typeClient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeClient should not enable id FormControl', () => {
        const formGroup = service.createTypeClientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeClient should disable id FormControl', () => {
        const formGroup = service.createTypeClientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
