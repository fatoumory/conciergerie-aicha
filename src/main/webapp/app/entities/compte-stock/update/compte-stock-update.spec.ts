import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { ICompteStock } from '../compte-stock.model';
import { CompteStockService } from '../service/compte-stock.service';

import { CompteStockFormService } from './compte-stock-form.service';
import { CompteStockUpdate } from './compte-stock-update';

describe('CompteStock Management Update Component', () => {
  let comp: CompteStockUpdate;
  let fixture: ComponentFixture<CompteStockUpdate>;
  let activatedRoute: ActivatedRoute;
  let compteStockFormService: CompteStockFormService;
  let compteStockService: CompteStockService;
  let serviceConciergerieService: ServiceConciergerieService;

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

    fixture = TestBed.createComponent(CompteStockUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compteStockFormService = TestBed.inject(CompteStockFormService);
    compteStockService = TestBed.inject(CompteStockService);
    serviceConciergerieService = TestBed.inject(ServiceConciergerieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call service query and add missing value', () => {
      const compteStock: ICompteStock = { id: 'f4e6718c-f6ee-4140-947c-bdbdef1eda55' };
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      compteStock.service = service;

      const serviceCollection: IServiceConciergerie[] = [{ id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' }];
      vi.spyOn(serviceConciergerieService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceCollection })));
      const expectedCollection: IServiceConciergerie[] = [service, ...serviceCollection];
      vi.spyOn(serviceConciergerieService, 'addServiceConciergerieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compteStock });
      comp.ngOnInit();

      expect(serviceConciergerieService.query).toHaveBeenCalled();
      expect(serviceConciergerieService.addServiceConciergerieToCollectionIfMissing).toHaveBeenCalledWith(serviceCollection, service);
      expect(comp.servicesCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const compteStock: ICompteStock = { id: 'f4e6718c-f6ee-4140-947c-bdbdef1eda55' };
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      compteStock.service = service;

      activatedRoute.data = of({ compteStock });
      comp.ngOnInit();

      expect(comp.servicesCollection()).toContainEqual(service);
      expect(comp.compteStock).toEqual(compteStock);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICompteStock>();
      const compteStock = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
      vi.spyOn(compteStockFormService, 'getCompteStock').mockReturnValue(compteStock);
      vi.spyOn(compteStockService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteStock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(compteStock);
      saveSubject.complete();

      // THEN
      expect(compteStockFormService.getCompteStock).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compteStockService.update).toHaveBeenCalledWith(expect.objectContaining(compteStock));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICompteStock>();
      const compteStock = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
      vi.spyOn(compteStockFormService, 'getCompteStock').mockReturnValue({ id: null });
      vi.spyOn(compteStockService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteStock: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(compteStock);
      saveSubject.complete();

      // THEN
      expect(compteStockFormService.getCompteStock).toHaveBeenCalled();
      expect(compteStockService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICompteStock>();
      const compteStock = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
      vi.spyOn(compteStockService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compteStock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compteStockService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServiceConciergerie', () => {
      it('should forward to serviceConciergerieService', () => {
        const entity = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
        const entity2 = { id: 'c043c152-745c-4486-b5a8-711daf859c96' };
        vi.spyOn(serviceConciergerieService, 'compareServiceConciergerie');
        comp.compareServiceConciergerie(entity, entity2);
        expect(serviceConciergerieService.compareServiceConciergerie).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
