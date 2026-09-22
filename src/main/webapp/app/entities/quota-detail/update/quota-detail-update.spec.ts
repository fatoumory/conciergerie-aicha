import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IQuotaService } from 'app/entities/quota-service/quota-service.model';
import { QuotaServiceService } from 'app/entities/quota-service/service/quota-service.service';
import { ZoneService } from 'app/entities/zone/service/zone.service';
import { IZone } from 'app/entities/zone/zone.model';
import { IQuotaDetail } from '../quota-detail.model';
import { QuotaDetailService } from '../service/quota-detail.service';

import { QuotaDetailFormService } from './quota-detail-form.service';
import { QuotaDetailUpdate } from './quota-detail-update';

describe('QuotaDetail Management Update Component', () => {
  let comp: QuotaDetailUpdate;
  let fixture: ComponentFixture<QuotaDetailUpdate>;
  let activatedRoute: ActivatedRoute;
  let quotaDetailFormService: QuotaDetailFormService;
  let quotaDetailService: QuotaDetailService;
  let quotaServiceService: QuotaServiceService;
  let zoneService: ZoneService;

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

    fixture = TestBed.createComponent(QuotaDetailUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quotaDetailFormService = TestBed.inject(QuotaDetailFormService);
    quotaDetailService = TestBed.inject(QuotaDetailService);
    quotaServiceService = TestBed.inject(QuotaServiceService);
    zoneService = TestBed.inject(ZoneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call QuotaService query and add missing value', () => {
      const quotaDetail: IQuotaDetail = { id: 'dcc90518-4e9b-457c-9452-a4b8a2b9176b' };
      const quotaService: IQuotaService = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
      quotaDetail.quotaService = quotaService;

      const quotaServiceCollection: IQuotaService[] = [{ id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' }];
      vi.spyOn(quotaServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: quotaServiceCollection })));
      const additionalQuotaServices = [quotaService];
      const expectedCollection: IQuotaService[] = [...additionalQuotaServices, ...quotaServiceCollection];
      vi.spyOn(quotaServiceService, 'addQuotaServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quotaDetail });
      comp.ngOnInit();

      expect(quotaServiceService.query).toHaveBeenCalled();
      expect(quotaServiceService.addQuotaServiceToCollectionIfMissing).toHaveBeenCalledWith(
        quotaServiceCollection,
        ...additionalQuotaServices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotaServicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Zone query and add missing value', () => {
      const quotaDetail: IQuotaDetail = { id: 'dcc90518-4e9b-457c-9452-a4b8a2b9176b' };
      const zone: IZone = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
      quotaDetail.zone = zone;

      const zoneCollection: IZone[] = [{ id: '876dfa56-710f-4774-ab70-e59edf28e4d6' }];
      vi.spyOn(zoneService, 'query').mockReturnValue(of(new HttpResponse({ body: zoneCollection })));
      const additionalZones = [zone];
      const expectedCollection: IZone[] = [...additionalZones, ...zoneCollection];
      vi.spyOn(zoneService, 'addZoneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quotaDetail });
      comp.ngOnInit();

      expect(zoneService.query).toHaveBeenCalled();
      expect(zoneService.addZoneToCollectionIfMissing).toHaveBeenCalledWith(
        zoneCollection,
        ...additionalZones.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.zonesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quotaDetail: IQuotaDetail = { id: 'dcc90518-4e9b-457c-9452-a4b8a2b9176b' };
      const quotaService: IQuotaService = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
      quotaDetail.quotaService = quotaService;
      const zone: IZone = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
      quotaDetail.zone = zone;

      activatedRoute.data = of({ quotaDetail });
      comp.ngOnInit();

      expect(comp.quotaServicesSharedCollection()).toContainEqual(quotaService);
      expect(comp.zonesSharedCollection()).toContainEqual(zone);
      expect(comp.quotaDetail).toEqual(quotaDetail);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuotaDetail>();
      const quotaDetail = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
      vi.spyOn(quotaDetailFormService, 'getQuotaDetail').mockReturnValue(quotaDetail);
      vi.spyOn(quotaDetailService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quotaDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quotaDetail);
      saveSubject.complete();

      // THEN
      expect(quotaDetailFormService.getQuotaDetail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quotaDetailService.update).toHaveBeenCalledWith(expect.objectContaining(quotaDetail));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuotaDetail>();
      const quotaDetail = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
      vi.spyOn(quotaDetailFormService, 'getQuotaDetail').mockReturnValue({ id: null });
      vi.spyOn(quotaDetailService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quotaDetail: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quotaDetail);
      saveSubject.complete();

      // THEN
      expect(quotaDetailFormService.getQuotaDetail).toHaveBeenCalled();
      expect(quotaDetailService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuotaDetail>();
      const quotaDetail = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
      vi.spyOn(quotaDetailService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quotaDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quotaDetailService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuotaService', () => {
      it('should forward to quotaServiceService', () => {
        const entity = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
        const entity2 = { id: '7f8f19e3-0d4a-4d13-b572-5b14477e5d1d' };
        vi.spyOn(quotaServiceService, 'compareQuotaService');
        comp.compareQuotaService(entity, entity2);
        expect(quotaServiceService.compareQuotaService).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareZone', () => {
      it('should forward to zoneService', () => {
        const entity = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
        const entity2 = { id: '49d119f6-ada1-4ddf-abe4-39f8231db1c3' };
        vi.spyOn(zoneService, 'compareZone');
        comp.compareZone(entity, entity2);
        expect(zoneService.compareZone).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
