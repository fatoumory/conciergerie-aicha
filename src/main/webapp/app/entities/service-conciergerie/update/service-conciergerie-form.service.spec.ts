import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../service-conciergerie.test-samples';

import { ServiceConciergerieFormService } from './service-conciergerie-form.service';

describe('ServiceConciergerie Form Service', () => {
  let service: ServiceConciergerieFormService;

  beforeEach(() => {
    service = TestBed.inject(ServiceConciergerieFormService);
  });

  describe('Service methods', () => {
    describe('createServiceConciergerieFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceConciergerieFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
            typeService: expect.any(Object),
          }),
        );
      });

      it('passing IServiceConciergerie should create a new form with FormGroup', () => {
        const formGroup = service.createServiceConciergerieFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
            typeService: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceConciergerie', () => {
      it('should return NewServiceConciergerie for default ServiceConciergerie initial value', () => {
        const formGroup = service.createServiceConciergerieFormGroup(sampleWithNewData);

        const serviceConciergerie = service.getServiceConciergerie(formGroup);

        expect(serviceConciergerie).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceConciergerie for empty ServiceConciergerie initial value', () => {
        const formGroup = service.createServiceConciergerieFormGroup();

        const serviceConciergerie = service.getServiceConciergerie(formGroup);

        expect(serviceConciergerie).toMatchObject({});
      });

      it('should return IServiceConciergerie', () => {
        const formGroup = service.createServiceConciergerieFormGroup(sampleWithRequiredData);

        const serviceConciergerie = service.getServiceConciergerie(formGroup);

        expect(serviceConciergerie).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceConciergerie should not enable id FormControl', () => {
        const formGroup = service.createServiceConciergerieFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceConciergerie should disable id FormControl', () => {
        const formGroup = service.createServiceConciergerieFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
