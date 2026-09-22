import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IQuotaDetail } from 'app/entities/quota-detail/quota-detail.model';
import { QuotaDetailService } from 'app/entities/quota-detail/service/quota-detail.service';
import { IQuotaService } from 'app/entities/quota-service/quota-service.model';
import { QuotaServiceService } from 'app/entities/quota-service/service/quota-service.service';
import { IConsommationQuota } from '../consommation-quota.model';
import { ConsommationQuotaService } from '../service/consommation-quota.service';

import { ConsommationQuotaFormService } from './consommation-quota-form.service';
import { ConsommationQuotaUpdate } from './consommation-quota-update';

describe('ConsommationQuota Management Update Component', () => {
  let comp: ConsommationQuotaUpdate;
  let fixture: ComponentFixture<ConsommationQuotaUpdate>;
  let activatedRoute: ActivatedRoute;
  let consommationQuotaFormService: ConsommationQuotaFormService;
  let consommationQuotaService: ConsommationQuotaService;
  let clientService: ClientService;
  let quotaServiceService: QuotaServiceService;
  let quotaDetailService: QuotaDetailService;

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

    fixture = TestBed.createComponent(ConsommationQuotaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consommationQuotaFormService = TestBed.inject(ConsommationQuotaFormService);
    consommationQuotaService = TestBed.inject(ConsommationQuotaService);
    clientService = TestBed.inject(ClientService);
    quotaServiceService = TestBed.inject(QuotaServiceService);
    quotaDetailService = TestBed.inject(QuotaDetailService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Client query and add missing value', () => {
      const consommationQuota: IConsommationQuota = { id: '8ed81fb1-e5e8-41ee-a12e-261e410b8c2a' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      consommationQuota.client = client;

      const clientCollection: IClient[] = [{ id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' }];
      vi.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      vi.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consommationQuota });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.clientsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call QuotaService query and add missing value', () => {
      const consommationQuota: IConsommationQuota = { id: '8ed81fb1-e5e8-41ee-a12e-261e410b8c2a' };
      const quotaService: IQuotaService = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
      consommationQuota.quotaService = quotaService;

      const quotaServiceCollection: IQuotaService[] = [{ id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' }];
      vi.spyOn(quotaServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: quotaServiceCollection })));
      const additionalQuotaServices = [quotaService];
      const expectedCollection: IQuotaService[] = [...additionalQuotaServices, ...quotaServiceCollection];
      vi.spyOn(quotaServiceService, 'addQuotaServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consommationQuota });
      comp.ngOnInit();

      expect(quotaServiceService.query).toHaveBeenCalled();
      expect(quotaServiceService.addQuotaServiceToCollectionIfMissing).toHaveBeenCalledWith(
        quotaServiceCollection,
        ...additionalQuotaServices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotaServicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call QuotaDetail query and add missing value', () => {
      const consommationQuota: IConsommationQuota = { id: '8ed81fb1-e5e8-41ee-a12e-261e410b8c2a' };
      const quotaDetail: IQuotaDetail = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
      consommationQuota.quotaDetail = quotaDetail;

      const quotaDetailCollection: IQuotaDetail[] = [{ id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' }];
      vi.spyOn(quotaDetailService, 'query').mockReturnValue(of(new HttpResponse({ body: quotaDetailCollection })));
      const additionalQuotaDetails = [quotaDetail];
      const expectedCollection: IQuotaDetail[] = [...additionalQuotaDetails, ...quotaDetailCollection];
      vi.spyOn(quotaDetailService, 'addQuotaDetailToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consommationQuota });
      comp.ngOnInit();

      expect(quotaDetailService.query).toHaveBeenCalled();
      expect(quotaDetailService.addQuotaDetailToCollectionIfMissing).toHaveBeenCalledWith(
        quotaDetailCollection,
        ...additionalQuotaDetails.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotaDetailsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const consommationQuota: IConsommationQuota = { id: '8ed81fb1-e5e8-41ee-a12e-261e410b8c2a' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      consommationQuota.client = client;
      const quotaService: IQuotaService = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
      consommationQuota.quotaService = quotaService;
      const quotaDetail: IQuotaDetail = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
      consommationQuota.quotaDetail = quotaDetail;

      activatedRoute.data = of({ consommationQuota });
      comp.ngOnInit();

      expect(comp.clientsSharedCollection()).toContainEqual(client);
      expect(comp.quotaServicesSharedCollection()).toContainEqual(quotaService);
      expect(comp.quotaDetailsSharedCollection()).toContainEqual(quotaDetail);
      expect(comp.consommationQuota).toEqual(consommationQuota);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IConsommationQuota>();
      const consommationQuota = { id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' };
      vi.spyOn(consommationQuotaFormService, 'getConsommationQuota').mockReturnValue(consommationQuota);
      vi.spyOn(consommationQuotaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consommationQuota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(consommationQuota);
      saveSubject.complete();

      // THEN
      expect(consommationQuotaFormService.getConsommationQuota).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consommationQuotaService.update).toHaveBeenCalledWith(expect.objectContaining(consommationQuota));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IConsommationQuota>();
      const consommationQuota = { id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' };
      vi.spyOn(consommationQuotaFormService, 'getConsommationQuota').mockReturnValue({ id: null });
      vi.spyOn(consommationQuotaService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consommationQuota: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(consommationQuota);
      saveSubject.complete();

      // THEN
      expect(consommationQuotaFormService.getConsommationQuota).toHaveBeenCalled();
      expect(consommationQuotaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IConsommationQuota>();
      const consommationQuota = { id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' };
      vi.spyOn(consommationQuotaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consommationQuota });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consommationQuotaService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClient', () => {
      it('should forward to clientService', () => {
        const entity = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
        const entity2 = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
        vi.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuotaService', () => {
      it('should forward to quotaServiceService', () => {
        const entity = { id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' };
        const entity2 = { id: '7f8f19e3-0d4a-4d13-b572-5b14477e5d1d' };
        vi.spyOn(quotaServiceService, 'compareQuotaService');
        comp.compareQuotaService(entity, entity2);
        expect(quotaServiceService.compareQuotaService).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuotaDetail', () => {
      it('should forward to quotaDetailService', () => {
        const entity = { id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' };
        const entity2 = { id: 'dcc90518-4e9b-457c-9452-a4b8a2b9176b' };
        vi.spyOn(quotaDetailService, 'compareQuotaDetail');
        comp.compareQuotaDetail(entity, entity2);
        expect(quotaDetailService.compareQuotaDetail).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
