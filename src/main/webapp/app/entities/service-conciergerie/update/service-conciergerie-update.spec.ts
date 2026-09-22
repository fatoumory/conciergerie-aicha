import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TypeServiceService } from 'app/entities/type-service/service/type-service.service';
import { ITypeService } from 'app/entities/type-service/type-service.model';
import { ServiceConciergerieService } from '../service/service-conciergerie.service';
import { IServiceConciergerie } from '../service-conciergerie.model';

import { ServiceConciergerieFormService } from './service-conciergerie-form.service';
import { ServiceConciergerieUpdate } from './service-conciergerie-update';

describe('ServiceConciergerie Management Update Component', () => {
  let comp: ServiceConciergerieUpdate;
  let fixture: ComponentFixture<ServiceConciergerieUpdate>;
  let activatedRoute: ActivatedRoute;
  let serviceConciergerieFormService: ServiceConciergerieFormService;
  let serviceConciergerieService: ServiceConciergerieService;
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

    fixture = TestBed.createComponent(ServiceConciergerieUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceConciergerieFormService = TestBed.inject(ServiceConciergerieFormService);
    serviceConciergerieService = TestBed.inject(ServiceConciergerieService);
    typeServiceService = TestBed.inject(TypeServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TypeService query and add missing value', () => {
      const serviceConciergerie: IServiceConciergerie = { id: 'c043c152-745c-4486-b5a8-711daf859c96' };
      const typeService: ITypeService = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
      serviceConciergerie.typeService = typeService;

      const typeServiceCollection: ITypeService[] = [{ id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' }];
      vi.spyOn(typeServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: typeServiceCollection })));
      const additionalTypeServices = [typeService];
      const expectedCollection: ITypeService[] = [...additionalTypeServices, ...typeServiceCollection];
      vi.spyOn(typeServiceService, 'addTypeServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceConciergerie });
      comp.ngOnInit();

      expect(typeServiceService.query).toHaveBeenCalled();
      expect(typeServiceService.addTypeServiceToCollectionIfMissing).toHaveBeenCalledWith(
        typeServiceCollection,
        ...additionalTypeServices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.typeServicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const serviceConciergerie: IServiceConciergerie = { id: 'c043c152-745c-4486-b5a8-711daf859c96' };
      const typeService: ITypeService = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
      serviceConciergerie.typeService = typeService;

      activatedRoute.data = of({ serviceConciergerie });
      comp.ngOnInit();

      expect(comp.typeServicesSharedCollection()).toContainEqual(typeService);
      expect(comp.serviceConciergerie).toEqual(serviceConciergerie);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServiceConciergerie>();
      const serviceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      vi.spyOn(serviceConciergerieFormService, 'getServiceConciergerie').mockReturnValue(serviceConciergerie);
      vi.spyOn(serviceConciergerieService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceConciergerie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(serviceConciergerie);
      saveSubject.complete();

      // THEN
      expect(serviceConciergerieFormService.getServiceConciergerie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceConciergerieService.update).toHaveBeenCalledWith(expect.objectContaining(serviceConciergerie));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServiceConciergerie>();
      const serviceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      vi.spyOn(serviceConciergerieFormService, 'getServiceConciergerie').mockReturnValue({ id: null });
      vi.spyOn(serviceConciergerieService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceConciergerie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(serviceConciergerie);
      saveSubject.complete();

      // THEN
      expect(serviceConciergerieFormService.getServiceConciergerie).toHaveBeenCalled();
      expect(serviceConciergerieService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IServiceConciergerie>();
      const serviceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      vi.spyOn(serviceConciergerieService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceConciergerie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceConciergerieService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeService', () => {
      it('should forward to typeServiceService', () => {
        const entity = { id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' };
        const entity2 = { id: 'ca7ba5b9-b039-4856-8f31-aa9428ca24bc' };
        vi.spyOn(typeServiceService, 'compareTypeService');
        comp.compareTypeService(entity, entity2);
        expect(typeServiceService.compareTypeService).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
