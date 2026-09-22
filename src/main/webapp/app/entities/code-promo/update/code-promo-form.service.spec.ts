import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../code-promo.test-samples';

import { CodePromoFormService } from './code-promo-form.service';

describe('CodePromo Form Service', () => {
  let service: CodePromoFormService;

  beforeEach(() => {
    service = TestBed.inject(CodePromoFormService);
  });

  describe('Service methods', () => {
    describe('createCodePromoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCodePromoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            valeur: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
          }),
        );
      });

      it('passing ICodePromo should create a new form with FormGroup', () => {
        const formGroup = service.createCodePromoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            valeur: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
          }),
        );
      });
    });

    describe('getCodePromo', () => {
      it('should return NewCodePromo for default CodePromo initial value', () => {
        const formGroup = service.createCodePromoFormGroup(sampleWithNewData);

        const codePromo = service.getCodePromo(formGroup);

        expect(codePromo).toMatchObject(sampleWithNewData);
      });

      it('should return NewCodePromo for empty CodePromo initial value', () => {
        const formGroup = service.createCodePromoFormGroup();

        const codePromo = service.getCodePromo(formGroup);

        expect(codePromo).toMatchObject({});
      });

      it('should return ICodePromo', () => {
        const formGroup = service.createCodePromoFormGroup(sampleWithRequiredData);

        const codePromo = service.getCodePromo(formGroup);

        expect(codePromo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICodePromo should not enable id FormControl', () => {
        const formGroup = service.createCodePromoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCodePromo should disable id FormControl', () => {
        const formGroup = service.createCodePromoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
