import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ISegmentClient } from '../segment-client.model';
import { SegmentClientService } from '../service/segment-client.service';

import { SegmentClientFormService } from './segment-client-form.service';
import { SegmentClientUpdate } from './segment-client-update';

describe('SegmentClient Management Update Component', () => {
  let comp: SegmentClientUpdate;
  let fixture: ComponentFixture<SegmentClientUpdate>;
  let activatedRoute: ActivatedRoute;
  let segmentClientFormService: SegmentClientFormService;
  let segmentClientService: SegmentClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(SegmentClientUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    segmentClientFormService = TestBed.inject(SegmentClientFormService);
    segmentClientService = TestBed.inject(SegmentClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const segmentClient: ISegmentClient = { id: '3dfe0be4-098d-4b25-9305-36dbaeacd2d1' };

      activatedRoute.data = of({ segmentClient });
      comp.ngOnInit();

      expect(comp.segmentClient).toEqual(segmentClient);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISegmentClient>();
      const segmentClient = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
      vi.spyOn(segmentClientFormService, 'getSegmentClient').mockReturnValue(segmentClient);
      vi.spyOn(segmentClientService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ segmentClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(segmentClient);
      saveSubject.complete();

      // THEN
      expect(segmentClientFormService.getSegmentClient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(segmentClientService.update).toHaveBeenCalledWith(expect.objectContaining(segmentClient));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISegmentClient>();
      const segmentClient = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
      vi.spyOn(segmentClientFormService, 'getSegmentClient').mockReturnValue({ id: null });
      vi.spyOn(segmentClientService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ segmentClient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(segmentClient);
      saveSubject.complete();

      // THEN
      expect(segmentClientFormService.getSegmentClient).toHaveBeenCalled();
      expect(segmentClientService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ISegmentClient>();
      const segmentClient = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
      vi.spyOn(segmentClientService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ segmentClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(segmentClientService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
