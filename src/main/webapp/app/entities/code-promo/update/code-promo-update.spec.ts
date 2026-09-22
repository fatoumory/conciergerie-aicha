import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICodePromo } from '../code-promo.model';
import { CodePromoService } from '../service/code-promo.service';

import { CodePromoFormService } from './code-promo-form.service';
import { CodePromoUpdate } from './code-promo-update';

describe('CodePromo Management Update Component', () => {
  let comp: CodePromoUpdate;
  let fixture: ComponentFixture<CodePromoUpdate>;
  let activatedRoute: ActivatedRoute;
  let codePromoFormService: CodePromoFormService;
  let codePromoService: CodePromoService;

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

    fixture = TestBed.createComponent(CodePromoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codePromoFormService = TestBed.inject(CodePromoFormService);
    codePromoService = TestBed.inject(CodePromoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const codePromo: ICodePromo = { id: '2f309410-f594-41bc-8818-5fbfb8f44a29' };

      activatedRoute.data = of({ codePromo });
      comp.ngOnInit();

      expect(comp.codePromo).toEqual(codePromo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICodePromo>();
      const codePromo = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
      vi.spyOn(codePromoFormService, 'getCodePromo').mockReturnValue(codePromo);
      vi.spyOn(codePromoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codePromo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(codePromo);
      saveSubject.complete();

      // THEN
      expect(codePromoFormService.getCodePromo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(codePromoService.update).toHaveBeenCalledWith(expect.objectContaining(codePromo));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICodePromo>();
      const codePromo = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
      vi.spyOn(codePromoFormService, 'getCodePromo').mockReturnValue({ id: null });
      vi.spyOn(codePromoService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codePromo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(codePromo);
      saveSubject.complete();

      // THEN
      expect(codePromoFormService.getCodePromo).toHaveBeenCalled();
      expect(codePromoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICodePromo>();
      const codePromo = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
      vi.spyOn(codePromoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codePromo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codePromoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
