import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TypeServiceService } from '../service/type-service.service';
import { ITypeService } from '../type-service.model';

import { TypeServiceFormService } from './type-service-form.service';
import { TypeServiceUpdate } from './type-service-update';

describe('TypeService Management Update Component', () => {
  let comp: TypeServiceUpdate;
  let fixture: ComponentFixture<TypeServiceUpdate>;
  let activatedRoute: ActivatedRoute;
  let typeServiceFormService: TypeServiceFormService;
  let typeServiceService: TypeServiceService;

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

    fixture = TestBed.createComponent(TypeServiceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeServiceFormService = TestBed.inject(TypeServiceFormService);
    typeServiceService = TestBed.inject(TypeServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const typeService: ITypeService = { id: 'ca7ba5b9-b039-4856-8f31-aa9428ca24bc' };

      activatedRoute.data = of({ typeService });
      comp.ngOnInit();

      expect(comp.typeService).toEqual(typeService);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeService>();
      const typeService = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
      vi.spyOn(typeServiceFormService, 'getTypeService').mockReturnValue(typeService);
      vi.spyOn(typeServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeService);
      saveSubject.complete();

      // THEN
      expect(typeServiceFormService.getTypeService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeServiceService.update).toHaveBeenCalledWith(expect.objectContaining(typeService));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeService>();
      const typeService = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
      vi.spyOn(typeServiceFormService, 'getTypeService').mockReturnValue({ id: null });
      vi.spyOn(typeServiceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeService);
      saveSubject.complete();

      // THEN
      expect(typeServiceFormService.getTypeService).toHaveBeenCalled();
      expect(typeServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeService>();
      const typeService = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
      vi.spyOn(typeServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeServiceService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
