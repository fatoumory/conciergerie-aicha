import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TypeDemandeService } from '../service/type-demande.service';
import { ITypeDemande } from '../type-demande.model';

import { TypeDemandeFormService } from './type-demande-form.service';
import { TypeDemandeUpdate } from './type-demande-update';

describe('TypeDemande Management Update Component', () => {
  let comp: TypeDemandeUpdate;
  let fixture: ComponentFixture<TypeDemandeUpdate>;
  let activatedRoute: ActivatedRoute;
  let typeDemandeFormService: TypeDemandeFormService;
  let typeDemandeService: TypeDemandeService;

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

    fixture = TestBed.createComponent(TypeDemandeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeDemandeFormService = TestBed.inject(TypeDemandeFormService);
    typeDemandeService = TestBed.inject(TypeDemandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const typeDemande: ITypeDemande = { id: 'b3d6689f-dd1c-49f0-b65c-9fee5dabd02e' };

      activatedRoute.data = of({ typeDemande });
      comp.ngOnInit();

      expect(comp.typeDemande).toEqual(typeDemande);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeDemande>();
      const typeDemande = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
      vi.spyOn(typeDemandeFormService, 'getTypeDemande').mockReturnValue(typeDemande);
      vi.spyOn(typeDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeDemande);
      saveSubject.complete();

      // THEN
      expect(typeDemandeFormService.getTypeDemande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeDemandeService.update).toHaveBeenCalledWith(expect.objectContaining(typeDemande));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeDemande>();
      const typeDemande = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
      vi.spyOn(typeDemandeFormService, 'getTypeDemande').mockReturnValue({ id: null });
      vi.spyOn(typeDemandeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeDemande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeDemande);
      saveSubject.complete();

      // THEN
      expect(typeDemandeFormService.getTypeDemande).toHaveBeenCalled();
      expect(typeDemandeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeDemande>();
      const typeDemande = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
      vi.spyOn(typeDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeDemandeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
