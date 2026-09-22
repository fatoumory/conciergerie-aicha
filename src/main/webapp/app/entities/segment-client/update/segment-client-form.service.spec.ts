import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../segment-client.test-samples';

import { SegmentClientFormService } from './segment-client-form.service';

describe('SegmentClient Form Service', () => {
  let service: SegmentClientFormService;

  beforeEach(() => {
    service = TestBed.inject(SegmentClientFormService);
  });

  describe('Service methods', () => {
    describe('createSegmentClientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSegmentClientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing ISegmentClient should create a new form with FormGroup', () => {
        const formGroup = service.createSegmentClientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getSegmentClient', () => {
      it('should return NewSegmentClient for default SegmentClient initial value', () => {
        const formGroup = service.createSegmentClientFormGroup(sampleWithNewData);

        const segmentClient = service.getSegmentClient(formGroup);

        expect(segmentClient).toMatchObject(sampleWithNewData);
      });

      it('should return NewSegmentClient for empty SegmentClient initial value', () => {
        const formGroup = service.createSegmentClientFormGroup();

        const segmentClient = service.getSegmentClient(formGroup);

        expect(segmentClient).toMatchObject({});
      });

      it('should return ISegmentClient', () => {
        const formGroup = service.createSegmentClientFormGroup(sampleWithRequiredData);

        const segmentClient = service.getSegmentClient(formGroup);

        expect(segmentClient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISegmentClient should not enable id FormControl', () => {
        const formGroup = service.createSegmentClientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSegmentClient should disable id FormControl', () => {
        const formGroup = service.createSegmentClientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
