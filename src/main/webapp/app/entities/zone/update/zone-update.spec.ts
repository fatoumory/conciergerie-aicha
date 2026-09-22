import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ZoneService } from '../service/zone.service';
import { IZone } from '../zone.model';

import { ZoneFormService } from './zone-form.service';
import { ZoneUpdate } from './zone-update';

describe('Zone Management Update Component', () => {
  let comp: ZoneUpdate;
  let fixture: ComponentFixture<ZoneUpdate>;
  let activatedRoute: ActivatedRoute;
  let zoneFormService: ZoneFormService;
  let zoneService: ZoneService;

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

    fixture = TestBed.createComponent(ZoneUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    zoneFormService = TestBed.inject(ZoneFormService);
    zoneService = TestBed.inject(ZoneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const zone: IZone = { id: '49d119f6-ada1-4ddf-abe4-39f8231db1c3' };

      activatedRoute.data = of({ zone });
      comp.ngOnInit();

      expect(comp.zone).toEqual(zone);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IZone>();
      const zone = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
      vi.spyOn(zoneFormService, 'getZone').mockReturnValue(zone);
      vi.spyOn(zoneService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(zone);
      saveSubject.complete();

      // THEN
      expect(zoneFormService.getZone).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(zoneService.update).toHaveBeenCalledWith(expect.objectContaining(zone));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IZone>();
      const zone = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
      vi.spyOn(zoneFormService, 'getZone').mockReturnValue({ id: null });
      vi.spyOn(zoneService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zone: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(zone);
      saveSubject.complete();

      // THEN
      expect(zoneFormService.getZone).toHaveBeenCalled();
      expect(zoneService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IZone>();
      const zone = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
      vi.spyOn(zoneService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ zone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(zoneService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
