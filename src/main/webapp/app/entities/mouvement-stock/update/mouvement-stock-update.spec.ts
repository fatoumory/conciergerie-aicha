import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICompteStock } from 'app/entities/compte-stock/compte-stock.model';
import { CompteStockService } from 'app/entities/compte-stock/service/compte-stock.service';
import { IMouvementStock } from '../mouvement-stock.model';
import { MouvementStockService } from '../service/mouvement-stock.service';

import { MouvementStockFormService } from './mouvement-stock-form.service';
import { MouvementStockUpdate } from './mouvement-stock-update';

describe('MouvementStock Management Update Component', () => {
  let comp: MouvementStockUpdate;
  let fixture: ComponentFixture<MouvementStockUpdate>;
  let activatedRoute: ActivatedRoute;
  let mouvementStockFormService: MouvementStockFormService;
  let mouvementStockService: MouvementStockService;
  let compteStockService: CompteStockService;

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

    fixture = TestBed.createComponent(MouvementStockUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mouvementStockFormService = TestBed.inject(MouvementStockFormService);
    mouvementStockService = TestBed.inject(MouvementStockService);
    compteStockService = TestBed.inject(CompteStockService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CompteStock query and add missing value', () => {
      const mouvementStock: IMouvementStock = { id: 'f9420f28-612d-4897-9d1b-342afbf4956c' };
      const compteStock: ICompteStock = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
      mouvementStock.compteStock = compteStock;

      const compteStockCollection: ICompteStock[] = [{ id: '51dcb663-66e6-485f-ad6e-94371c61b67e' }];
      vi.spyOn(compteStockService, 'query').mockReturnValue(of(new HttpResponse({ body: compteStockCollection })));
      const additionalCompteStocks = [compteStock];
      const expectedCollection: ICompteStock[] = [...additionalCompteStocks, ...compteStockCollection];
      vi.spyOn(compteStockService, 'addCompteStockToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mouvementStock });
      comp.ngOnInit();

      expect(compteStockService.query).toHaveBeenCalled();
      expect(compteStockService.addCompteStockToCollectionIfMissing).toHaveBeenCalledWith(
        compteStockCollection,
        ...additionalCompteStocks.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.compteStocksSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const mouvementStock: IMouvementStock = { id: 'f9420f28-612d-4897-9d1b-342afbf4956c' };
      const compteStock: ICompteStock = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
      mouvementStock.compteStock = compteStock;

      activatedRoute.data = of({ mouvementStock });
      comp.ngOnInit();

      expect(comp.compteStocksSharedCollection()).toContainEqual(compteStock);
      expect(comp.mouvementStock).toEqual(mouvementStock);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMouvementStock>();
      const mouvementStock = { id: 'fc5bd5ed-2dc4-439b-bdbd-8c3315d903f7' };
      vi.spyOn(mouvementStockFormService, 'getMouvementStock').mockReturnValue(mouvementStock);
      vi.spyOn(mouvementStockService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mouvementStock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(mouvementStock);
      saveSubject.complete();

      // THEN
      expect(mouvementStockFormService.getMouvementStock).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mouvementStockService.update).toHaveBeenCalledWith(expect.objectContaining(mouvementStock));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMouvementStock>();
      const mouvementStock = { id: 'fc5bd5ed-2dc4-439b-bdbd-8c3315d903f7' };
      vi.spyOn(mouvementStockFormService, 'getMouvementStock').mockReturnValue({ id: null });
      vi.spyOn(mouvementStockService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mouvementStock: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(mouvementStock);
      saveSubject.complete();

      // THEN
      expect(mouvementStockFormService.getMouvementStock).toHaveBeenCalled();
      expect(mouvementStockService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IMouvementStock>();
      const mouvementStock = { id: 'fc5bd5ed-2dc4-439b-bdbd-8c3315d903f7' };
      vi.spyOn(mouvementStockService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mouvementStock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mouvementStockService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCompteStock', () => {
      it('should forward to compteStockService', () => {
        const entity = { id: '51dcb663-66e6-485f-ad6e-94371c61b67e' };
        const entity2 = { id: 'f4e6718c-f6ee-4140-947c-bdbdef1eda55' };
        vi.spyOn(compteStockService, 'compareCompteStock');
        comp.compareCompteStock(entity, entity2);
        expect(compteStockService.compareCompteStock).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
