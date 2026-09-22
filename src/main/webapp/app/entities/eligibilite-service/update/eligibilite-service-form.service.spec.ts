import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../eligibilite-service.test-samples';

import { EligibiliteServiceFormService } from './eligibilite-service-form.service';

describe('EligibiliteService Form Service', () => {
  let service: EligibiliteServiceFormService;

  beforeEach(() => {
    service = TestBed.inject(EligibiliteServiceFormService);
  });

  describe('Service methods', () => {
    describe('createEligibiliteServiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEligibiliteServiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            autorise: expect.any(Object),
            gratuit: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            service: expect.any(Object),
            segmentClient: expect.any(Object),
            typeClient: expect.any(Object),
          }),
        );
      });

      it('passing IEligibiliteService should create a new form with FormGroup', () => {
        const formGroup = service.createEligibiliteServiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            autorise: expect.any(Object),
            gratuit: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            service: expect.any(Object),
            segmentClient: expect.any(Object),
            typeClient: expect.any(Object),
          }),
        );
      });
    });

    describe('getEligibiliteService', () => {
      it('should return NewEligibiliteService for default EligibiliteService initial value', () => {
        const formGroup = service.createEligibiliteServiceFormGroup(sampleWithNewData);

        const eligibiliteService = service.getEligibiliteService(formGroup);

        expect(eligibiliteService).toMatchObject(sampleWithNewData);
      });

      it('should return NewEligibiliteService for empty EligibiliteService initial value', () => {
        const formGroup = service.createEligibiliteServiceFormGroup();

        const eligibiliteService = service.getEligibiliteService(formGroup);

        expect(eligibiliteService).toMatchObject({});
      });

      it('should return IEligibiliteService', () => {
        const formGroup = service.createEligibiliteServiceFormGroup(sampleWithRequiredData);

        const eligibiliteService = service.getEligibiliteService(formGroup);

        expect(eligibiliteService).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEligibiliteService should not enable id FormControl', () => {
        const formGroup = service.createEligibiliteServiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEligibiliteService should disable id FormControl', () => {
        const formGroup = service.createEligibiliteServiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
