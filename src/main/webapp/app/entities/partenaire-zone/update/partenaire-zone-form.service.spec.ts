import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../partenaire-zone.test-samples';

import { PartenaireZoneFormService } from './partenaire-zone-form.service';

describe('PartenaireZone Form Service', () => {
  let service: PartenaireZoneFormService;

  beforeEach(() => {
    service = TestBed.inject(PartenaireZoneFormService);
  });

  describe('Service methods', () => {
    describe('createPartenaireZoneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartenaireZoneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            partenaire: expect.any(Object),
            zone: expect.any(Object),
          }),
        );
      });

      it('passing IPartenaireZone should create a new form with FormGroup', () => {
        const formGroup = service.createPartenaireZoneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            partenaire: expect.any(Object),
            zone: expect.any(Object),
          }),
        );
      });
    });

    describe('getPartenaireZone', () => {
      it('should return NewPartenaireZone for default PartenaireZone initial value', () => {
        const formGroup = service.createPartenaireZoneFormGroup(sampleWithNewData);

        const partenaireZone = service.getPartenaireZone(formGroup);

        expect(partenaireZone).toMatchObject(sampleWithNewData);
      });

      it('should return NewPartenaireZone for empty PartenaireZone initial value', () => {
        const formGroup = service.createPartenaireZoneFormGroup();

        const partenaireZone = service.getPartenaireZone(formGroup);

        expect(partenaireZone).toMatchObject({});
      });

      it('should return IPartenaireZone', () => {
        const formGroup = service.createPartenaireZoneFormGroup(sampleWithRequiredData);

        const partenaireZone = service.getPartenaireZone(formGroup);

        expect(partenaireZone).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPartenaireZone should not enable id FormControl', () => {
        const formGroup = service.createPartenaireZoneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPartenaireZone should disable id FormControl', () => {
        const formGroup = service.createPartenaireZoneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
