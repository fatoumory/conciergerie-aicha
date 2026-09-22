import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TypeClientService } from '../service/type-client.service';
import { ITypeClient } from '../type-client.model';

import { TypeClientFormService } from './type-client-form.service';
import { TypeClientUpdate } from './type-client-update';

describe('TypeClient Management Update Component', () => {
  let comp: TypeClientUpdate;
  let fixture: ComponentFixture<TypeClientUpdate>;
  let activatedRoute: ActivatedRoute;
  let typeClientFormService: TypeClientFormService;
  let typeClientService: TypeClientService;

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

    fixture = TestBed.createComponent(TypeClientUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeClientFormService = TestBed.inject(TypeClientFormService);
    typeClientService = TestBed.inject(TypeClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const typeClient: ITypeClient = { id: 'c3bcad5f-3b94-4cfe-b1a6-4c3b86b45e40' };

      activatedRoute.data = of({ typeClient });
      comp.ngOnInit();

      expect(comp.typeClient).toEqual(typeClient);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeClient>();
      const typeClient = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
      vi.spyOn(typeClientFormService, 'getTypeClient').mockReturnValue(typeClient);
      vi.spyOn(typeClientService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeClient);
      saveSubject.complete();

      // THEN
      expect(typeClientFormService.getTypeClient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeClientService.update).toHaveBeenCalledWith(expect.objectContaining(typeClient));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeClient>();
      const typeClient = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
      vi.spyOn(typeClientFormService, 'getTypeClient').mockReturnValue({ id: null });
      vi.spyOn(typeClientService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeClient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeClient);
      saveSubject.complete();

      // THEN
      expect(typeClientFormService.getTypeClient).toHaveBeenCalled();
      expect(typeClientService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeClient>();
      const typeClient = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
      vi.spyOn(typeClientService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeClient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeClientService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
