import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IIdempotencyKey } from '../idempotency-key.model';
import { IdempotencyKeyService } from '../service/idempotency-key.service';

import { IdempotencyKeyFormService } from './idempotency-key-form.service';
import { IdempotencyKeyUpdate } from './idempotency-key-update';

describe('IdempotencyKey Management Update Component', () => {
  let comp: IdempotencyKeyUpdate;
  let fixture: ComponentFixture<IdempotencyKeyUpdate>;
  let activatedRoute: ActivatedRoute;
  let idempotencyKeyFormService: IdempotencyKeyFormService;
  let idempotencyKeyService: IdempotencyKeyService;

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

    fixture = TestBed.createComponent(IdempotencyKeyUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    idempotencyKeyFormService = TestBed.inject(IdempotencyKeyFormService);
    idempotencyKeyService = TestBed.inject(IdempotencyKeyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const idempotencyKey: IIdempotencyKey = { id: 'c81b889c-e72a-4127-89b5-2c11138dd9d2' };

      activatedRoute.data = of({ idempotencyKey });
      comp.ngOnInit();

      expect(comp.idempotencyKey).toEqual(idempotencyKey);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IIdempotencyKey>();
      const idempotencyKey = { id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' };
      vi.spyOn(idempotencyKeyFormService, 'getIdempotencyKey').mockReturnValue(idempotencyKey);
      vi.spyOn(idempotencyKeyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idempotencyKey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(idempotencyKey);
      saveSubject.complete();

      // THEN
      expect(idempotencyKeyFormService.getIdempotencyKey).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(idempotencyKeyService.update).toHaveBeenCalledWith(expect.objectContaining(idempotencyKey));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IIdempotencyKey>();
      const idempotencyKey = { id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' };
      vi.spyOn(idempotencyKeyFormService, 'getIdempotencyKey').mockReturnValue({ id: null });
      vi.spyOn(idempotencyKeyService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idempotencyKey: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(idempotencyKey);
      saveSubject.complete();

      // THEN
      expect(idempotencyKeyFormService.getIdempotencyKey).toHaveBeenCalled();
      expect(idempotencyKeyService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IIdempotencyKey>();
      const idempotencyKey = { id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' };
      vi.spyOn(idempotencyKeyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idempotencyKey });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(idempotencyKeyService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
