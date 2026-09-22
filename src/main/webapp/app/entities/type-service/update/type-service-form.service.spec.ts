import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../type-service.test-samples';

import { TypeServiceFormService } from './type-service-form.service';

describe('TypeService Form Service', () => {
  let service: TypeServiceFormService;

  beforeEach(() => {
    service = TestBed.inject(TypeServiceFormService);
  });

  describe('Service methods', () => {
    describe('createTypeServiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeServiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing ITypeService should create a new form with FormGroup', () => {
        const formGroup = service.createTypeServiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeService', () => {
      it('should return NewTypeService for default TypeService initial value', () => {
        const formGroup = service.createTypeServiceFormGroup(sampleWithNewData);

        const typeService = service.getTypeService(formGroup);

        expect(typeService).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeService for empty TypeService initial value', () => {
        const formGroup = service.createTypeServiceFormGroup();

        const typeService = service.getTypeService(formGroup);

        expect(typeService).toMatchObject({});
      });

      it('should return ITypeService', () => {
        const formGroup = service.createTypeServiceFormGroup(sampleWithRequiredData);

        const typeService = service.getTypeService(formGroup);

        expect(typeService).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeService should not enable id FormControl', () => {
        const formGroup = service.createTypeServiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeService should disable id FormControl', () => {
        const formGroup = service.createTypeServiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
