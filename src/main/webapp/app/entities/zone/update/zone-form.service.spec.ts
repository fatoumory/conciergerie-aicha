import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../zone.test-samples';

import { ZoneFormService } from './zone-form.service';

describe('Zone Form Service', () => {
  let service: ZoneFormService;

  beforeEach(() => {
    service = TestBed.inject(ZoneFormService);
  });

  describe('Service methods', () => {
    describe('createZoneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createZoneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing IZone should create a new form with FormGroup', () => {
        const formGroup = service.createZoneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getZone', () => {
      it('should return NewZone for default Zone initial value', () => {
        const formGroup = service.createZoneFormGroup(sampleWithNewData);

        const zone = service.getZone(formGroup);

        expect(zone).toMatchObject(sampleWithNewData);
      });

      it('should return NewZone for empty Zone initial value', () => {
        const formGroup = service.createZoneFormGroup();

        const zone = service.getZone(formGroup);

        expect(zone).toMatchObject({});
      });

      it('should return IZone', () => {
        const formGroup = service.createZoneFormGroup(sampleWithRequiredData);

        const zone = service.getZone(formGroup);

        expect(zone).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IZone should not enable id FormControl', () => {
        const formGroup = service.createZoneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewZone should disable id FormControl', () => {
        const formGroup = service.createZoneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
