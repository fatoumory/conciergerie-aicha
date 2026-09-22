import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../evaluation.test-samples';

import { EvaluationFormService } from './evaluation-form.service';

describe('Evaluation Form Service', () => {
  let service: EvaluationFormService;

  beforeEach(() => {
    service = TestBed.inject(EvaluationFormService);
  });

  describe('Service methods', () => {
    describe('createEvaluationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvaluationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            commentaire: expect.any(Object),
            dateEvaluation: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });

      it('passing IEvaluation should create a new form with FormGroup', () => {
        const formGroup = service.createEvaluationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            commentaire: expect.any(Object),
            dateEvaluation: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });
    });

    describe('getEvaluation', () => {
      it('should return NewEvaluation for default Evaluation initial value', () => {
        const formGroup = service.createEvaluationFormGroup(sampleWithNewData);

        const evaluation = service.getEvaluation(formGroup);

        expect(evaluation).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvaluation for empty Evaluation initial value', () => {
        const formGroup = service.createEvaluationFormGroup();

        const evaluation = service.getEvaluation(formGroup);

        expect(evaluation).toMatchObject({});
      });

      it('should return IEvaluation', () => {
        const formGroup = service.createEvaluationFormGroup(sampleWithRequiredData);

        const evaluation = service.getEvaluation(formGroup);

        expect(evaluation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvaluation should not enable id FormControl', () => {
        const formGroup = service.createEvaluationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvaluation should disable id FormControl', () => {
        const formGroup = service.createEvaluationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
