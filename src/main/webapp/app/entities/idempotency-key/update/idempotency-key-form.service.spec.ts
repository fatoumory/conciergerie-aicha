import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../idempotency-key.test-samples';

import { IdempotencyKeyFormService } from './idempotency-key-form.service';

describe('IdempotencyKey Form Service', () => {
  let service: IdempotencyKeyFormService;

  beforeEach(() => {
    service = TestBed.inject(IdempotencyKeyFormService);
  });

  describe('Service methods', () => {
    describe('createIdempotencyKeyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIdempotencyKeyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cle: expect.any(Object),
            typeOperation: expect.any(Object),
            resourceId: expect.any(Object),
            dateCreation: expect.any(Object),
            dateExpiration: expect.any(Object),
          }),
        );
      });

      it('passing IIdempotencyKey should create a new form with FormGroup', () => {
        const formGroup = service.createIdempotencyKeyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cle: expect.any(Object),
            typeOperation: expect.any(Object),
            resourceId: expect.any(Object),
            dateCreation: expect.any(Object),
            dateExpiration: expect.any(Object),
          }),
        );
      });
    });

    describe('getIdempotencyKey', () => {
      it('should return NewIdempotencyKey for default IdempotencyKey initial value', () => {
        const formGroup = service.createIdempotencyKeyFormGroup(sampleWithNewData);

        const idempotencyKey = service.getIdempotencyKey(formGroup);

        expect(idempotencyKey).toMatchObject(sampleWithNewData);
      });

      it('should return NewIdempotencyKey for empty IdempotencyKey initial value', () => {
        const formGroup = service.createIdempotencyKeyFormGroup();

        const idempotencyKey = service.getIdempotencyKey(formGroup);

        expect(idempotencyKey).toMatchObject({});
      });

      it('should return IIdempotencyKey', () => {
        const formGroup = service.createIdempotencyKeyFormGroup(sampleWithRequiredData);

        const idempotencyKey = service.getIdempotencyKey(formGroup);

        expect(idempotencyKey).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIdempotencyKey should not enable id FormControl', () => {
        const formGroup = service.createIdempotencyKeyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIdempotencyKey should disable id FormControl', () => {
        const formGroup = service.createIdempotencyKeyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
