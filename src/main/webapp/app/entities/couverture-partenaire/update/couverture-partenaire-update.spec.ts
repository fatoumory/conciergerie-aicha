import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { ICouverturePartenaire } from '../couverture-partenaire.model';
import { CouverturePartenaireService } from '../service/couverture-partenaire.service';

import { CouverturePartenaireFormService } from './couverture-partenaire-form.service';
import { CouverturePartenaireUpdate } from './couverture-partenaire-update';

describe('CouverturePartenaire Management Update Component', () => {
  let comp: CouverturePartenaireUpdate;
  let fixture: ComponentFixture<CouverturePartenaireUpdate>;
  let activatedRoute: ActivatedRoute;
  let couverturePartenaireFormService: CouverturePartenaireFormService;
  let couverturePartenaireService: CouverturePartenaireService;
  let partenaireService: PartenaireService;
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

    fixture = TestBed.createComponent(CouverturePartenaireUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    couverturePartenaireFormService = TestBed.inject(CouverturePartenaireFormService);
    couverturePartenaireService = TestBed.inject(CouverturePartenaireService);
    partenaireService = TestBed.inject(PartenaireService);
    serviceConciergerieService = TestBed.inject(ServiceConciergerieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Partenaire query and add missing value', () => {
      const couverturePartenaire: ICouverturePartenaire = { id: '076fcf94-5a8d-4425-978b-756cd4b9bd18' };
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      couverturePartenaire.partenaire = partenaire;

      const partenaireCollection: IPartenaire[] = [{ id: 'd330918e-c880-4b4f-a4ba-30a19287c322' }];
      vi.spyOn(partenaireService, 'query').mockReturnValue(of(new HttpResponse({ body: partenaireCollection })));
      const additionalPartenaires = [partenaire];
      const expectedCollection: IPartenaire[] = [...additionalPartenaires, ...partenaireCollection];
      vi.spyOn(partenaireService, 'addPartenaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ couverturePartenaire });
      comp.ngOnInit();

      expect(partenaireService.query).toHaveBeenCalled();
      expect(partenaireService.addPartenaireToCollectionIfMissing).toHaveBeenCalledWith(
        partenaireCollection,
        ...additionalPartenaires.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partenairesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ServiceConciergerie query and add missing value', () => {
      const couverturePartenaire: ICouverturePartenaire = { id: '076fcf94-5a8d-4425-978b-756cd4b9bd18' };
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      couverturePartenaire.service = service;

      const serviceConciergerieCollection: IServiceConciergerie[] = [{ id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' }];
      vi.spyOn(serviceConciergerieService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceConciergerieCollection })));
      const additionalServiceConciergeries = [service];
      const expectedCollection: IServiceConciergerie[] = [...additionalServiceConciergeries, ...serviceConciergerieCollection];
      vi.spyOn(serviceConciergerieService, 'addServiceConciergerieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ couverturePartenaire });
      comp.ngOnInit();

      expect(serviceConciergerieService.query).toHaveBeenCalled();
      expect(serviceConciergerieService.addServiceConciergerieToCollectionIfMissing).toHaveBeenCalledWith(
        serviceConciergerieCollection,
        ...additionalServiceConciergeries.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviceConciergeriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const couverturePartenaire: ICouverturePartenaire = { id: '076fcf94-5a8d-4425-978b-756cd4b9bd18' };
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      couverturePartenaire.partenaire = partenaire;
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      couverturePartenaire.service = service;

      activatedRoute.data = of({ couverturePartenaire });
      comp.ngOnInit();

      expect(comp.partenairesSharedCollection()).toContainEqual(partenaire);
      expect(comp.serviceConciergeriesSharedCollection()).toContainEqual(service);
      expect(comp.couverturePartenaire).toEqual(couverturePartenaire);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICouverturePartenaire>();
      const couverturePartenaire = { id: 'ad78c8f4-7567-47d0-9aac-cdd545c947f8' };
      vi.spyOn(couverturePartenaireFormService, 'getCouverturePartenaire').mockReturnValue(couverturePartenaire);
      vi.spyOn(couverturePartenaireService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couverturePartenaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(couverturePartenaire);
      saveSubject.complete();

      // THEN
      expect(couverturePartenaireFormService.getCouverturePartenaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(couverturePartenaireService.update).toHaveBeenCalledWith(expect.objectContaining(couverturePartenaire));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICouverturePartenaire>();
      const couverturePartenaire = { id: 'ad78c8f4-7567-47d0-9aac-cdd545c947f8' };
      vi.spyOn(couverturePartenaireFormService, 'getCouverturePartenaire').mockReturnValue({ id: null });
      vi.spyOn(couverturePartenaireService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couverturePartenaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(couverturePartenaire);
      saveSubject.complete();

      // THEN
      expect(couverturePartenaireFormService.getCouverturePartenaire).toHaveBeenCalled();
      expect(couverturePartenaireService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICouverturePartenaire>();
      const couverturePartenaire = { id: 'ad78c8f4-7567-47d0-9aac-cdd545c947f8' };
      vi.spyOn(couverturePartenaireService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couverturePartenaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(couverturePartenaireService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePartenaire', () => {
      it('should forward to partenaireService', () => {
        const entity = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
        const entity2 = { id: 'b6fc1546-6825-402d-9e00-2c8ab08f6246' };
        vi.spyOn(partenaireService, 'comparePartenaire');
        comp.comparePartenaire(entity, entity2);
        expect(partenaireService.comparePartenaire).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
