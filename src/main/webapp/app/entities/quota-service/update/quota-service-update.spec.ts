import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IEligibiliteService } from 'app/entities/eligibilite-service/eligibilite-service.model';
import { EligibiliteServiceService } from 'app/entities/eligibilite-service/service/eligibilite-service.service';
import { IQuotaService } from '../quota-service.model';
import { QuotaServiceService } from '../service/quota-service.service';

import { QuotaServiceFormService } from './quota-service-form.service';
import { QuotaServiceUpdate } from './quota-service-update';

describe('QuotaService Management Update Component', () => {
  let comp: QuotaServiceUpdate;
  let fixture: ComponentFixture<QuotaServiceUpdate>;
  let activatedRoute: ActivatedRoute;
  let quotaServiceFormService: QuotaServiceFormService;
  let quotaServiceService: QuotaServiceService;
  let eligibiliteServiceService: EligibiliteServiceService;

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

    fixture = TestBed.createComponent(QuotaServiceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quotaServiceFormService = TestBed.inject(QuotaServiceFormService);
    quotaServiceService = TestBed.inject(QuotaServiceService);
    eligibiliteServiceService = TestBed.inject(EligibiliteServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call EligibiliteService query and add missing value', () => {
      const quotaService: IQuotaService = { id: '7f8f19e3-0d4a-4d13-b572-5b14477e5d1d' };
      const eligibiliteService: IEligibiliteService = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
      quotaService.eligibiliteService = eligibiliteService;

      const eligibiliteServiceCollection: IEligibiliteService[] = [{ id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' }];
      vi.spyOn(eligibiliteServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: eligibiliteServiceCollection })));
      const additionalEligibiliteServices = [eligibiliteService];
      const expectedCollection: IEligibiliteService[] = [...additionalEligibiliteServices, ...eligibiliteServiceCollection];
      vi.spyOn(eligibiliteServiceService, 'addEligibiliteServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quotaService });
      comp.ngOnInit();

      expect(eligibiliteServiceService.query).toHaveBeenCalled();
      expect(eligibiliteServiceService.addEligibiliteServiceToCollectionIfMissing).toHaveBeenCalledWith(
        eligibiliteServiceCollection,
        ...additionalEligibiliteServices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.eligibiliteServicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quotaService: IQuotaService = { id: '7f8f19e3-0d4a-4d13-b572-5b14477e5d1d' };
      const eligibiliteService: IEligibiliteService = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
      quotaService.eligibiliteService = eligibiliteService;

      activatedRoute.data = of({ quotaService });
      comp.ngOnInit();

      expect(comp.eligibiliteServicesSharedCollection()).toContainEqual(eligibiliteService);
      expect(comp.quotaService).toEqual(quotaService);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuotaService>();
      const quotaService = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
      vi.spyOn(quotaServiceFormService, 'getQuotaService').mockReturnValue(quotaService);
      vi.spyOn(quotaServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quotaService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quotaService);
      saveSubject.complete();

      // THEN
      expect(quotaServiceFormService.getQuotaService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quotaServiceService.update).toHaveBeenCalledWith(expect.objectContaining(quotaService));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuotaService>();
      const quotaService = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
      vi.spyOn(quotaServiceFormService, 'getQuotaService').mockReturnValue({ id: null });
      vi.spyOn(quotaServiceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quotaService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quotaService);
      saveSubject.complete();

      // THEN
      expect(quotaServiceFormService.getQuotaService).toHaveBeenCalled();
      expect(quotaServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuotaService>();
      const quotaService = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
      vi.spyOn(quotaServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quotaService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quotaServiceService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEligibiliteService', () => {
      it('should forward to eligibiliteServiceService', () => {
        const entity = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
        const entity2 = { id: 'e70583f6-81ec-4ba0-bc9f-d0bbd7deed39' };
        vi.spyOn(eligibiliteServiceService, 'compareEligibiliteService');
        comp.compareEligibiliteService(entity, entity2);
        expect(eligibiliteServiceService.compareEligibiliteService).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
