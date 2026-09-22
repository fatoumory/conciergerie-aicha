import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ISegmentClient } from 'app/entities/segment-client/segment-client.model';
import { SegmentClientService } from 'app/entities/segment-client/service/segment-client.service';
import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { TypeClientService } from 'app/entities/type-client/service/type-client.service';
import { ITypeClient } from 'app/entities/type-client/type-client.model';
import { IEligibiliteService } from '../eligibilite-service.model';
import { EligibiliteServiceService } from '../service/eligibilite-service.service';

import { EligibiliteServiceFormService } from './eligibilite-service-form.service';
import { EligibiliteServiceUpdate } from './eligibilite-service-update';

describe('EligibiliteService Management Update Component', () => {
  let comp: EligibiliteServiceUpdate;
  let fixture: ComponentFixture<EligibiliteServiceUpdate>;
  let activatedRoute: ActivatedRoute;
  let eligibiliteServiceFormService: EligibiliteServiceFormService;
  let eligibiliteServiceService: EligibiliteServiceService;
  let serviceConciergerieService: ServiceConciergerieService;
  let segmentClientService: SegmentClientService;
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

    fixture = TestBed.createComponent(EligibiliteServiceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eligibiliteServiceFormService = TestBed.inject(EligibiliteServiceFormService);
    eligibiliteServiceService = TestBed.inject(EligibiliteServiceService);
    serviceConciergerieService = TestBed.inject(ServiceConciergerieService);
    segmentClientService = TestBed.inject(SegmentClientService);
    typeClientService = TestBed.inject(TypeClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ServiceConciergerie query and add missing value', () => {
      const eligibiliteService: IEligibiliteService = { id: 'e70583f6-81ec-4ba0-bc9f-d0bbd7deed39' };
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      eligibiliteService.service = service;

      const serviceConciergerieCollection: IServiceConciergerie[] = [{ id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' }];
      vi.spyOn(serviceConciergerieService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceConciergerieCollection })));
      const additionalServiceConciergeries = [service];
      const expectedCollection: IServiceConciergerie[] = [...additionalServiceConciergeries, ...serviceConciergerieCollection];
      vi.spyOn(serviceConciergerieService, 'addServiceConciergerieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eligibiliteService });
      comp.ngOnInit();

      expect(serviceConciergerieService.query).toHaveBeenCalled();
      expect(serviceConciergerieService.addServiceConciergerieToCollectionIfMissing).toHaveBeenCalledWith(
        serviceConciergerieCollection,
        ...additionalServiceConciergeries.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviceConciergeriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call SegmentClient query and add missing value', () => {
      const eligibiliteService: IEligibiliteService = { id: 'e70583f6-81ec-4ba0-bc9f-d0bbd7deed39' };
      const segmentClient: ISegmentClient = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
      eligibiliteService.segmentClient = segmentClient;

      const segmentClientCollection: ISegmentClient[] = [{ id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' }];
      vi.spyOn(segmentClientService, 'query').mockReturnValue(of(new HttpResponse({ body: segmentClientCollection })));
      const additionalSegmentClients = [segmentClient];
      const expectedCollection: ISegmentClient[] = [...additionalSegmentClients, ...segmentClientCollection];
      vi.spyOn(segmentClientService, 'addSegmentClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eligibiliteService });
      comp.ngOnInit();

      expect(segmentClientService.query).toHaveBeenCalled();
      expect(segmentClientService.addSegmentClientToCollectionIfMissing).toHaveBeenCalledWith(
        segmentClientCollection,
        ...additionalSegmentClients.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.segmentClientsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TypeClient query and add missing value', () => {
      const eligibiliteService: IEligibiliteService = { id: 'e70583f6-81ec-4ba0-bc9f-d0bbd7deed39' };
      const typeClient: ITypeClient = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
      eligibiliteService.typeClient = typeClient;

      const typeClientCollection: ITypeClient[] = [{ id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' }];
      vi.spyOn(typeClientService, 'query').mockReturnValue(of(new HttpResponse({ body: typeClientCollection })));
      const additionalTypeClients = [typeClient];
      const expectedCollection: ITypeClient[] = [...additionalTypeClients, ...typeClientCollection];
      vi.spyOn(typeClientService, 'addTypeClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eligibiliteService });
      comp.ngOnInit();

      expect(typeClientService.query).toHaveBeenCalled();
      expect(typeClientService.addTypeClientToCollectionIfMissing).toHaveBeenCalledWith(
        typeClientCollection,
        ...additionalTypeClients.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.typeClientsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const eligibiliteService: IEligibiliteService = { id: 'e70583f6-81ec-4ba0-bc9f-d0bbd7deed39' };
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      eligibiliteService.service = service;
      const segmentClient: ISegmentClient = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
      eligibiliteService.segmentClient = segmentClient;
      const typeClient: ITypeClient = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
      eligibiliteService.typeClient = typeClient;

      activatedRoute.data = of({ eligibiliteService });
      comp.ngOnInit();

      expect(comp.serviceConciergeriesSharedCollection()).toContainEqual(service);
      expect(comp.segmentClientsSharedCollection()).toContainEqual(segmentClient);
      expect(comp.typeClientsSharedCollection()).toContainEqual(typeClient);
      expect(comp.eligibiliteService).toEqual(eligibiliteService);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEligibiliteService>();
      const eligibiliteService = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
      vi.spyOn(eligibiliteServiceFormService, 'getEligibiliteService').mockReturnValue(eligibiliteService);
      vi.spyOn(eligibiliteServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eligibiliteService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(eligibiliteService);
      saveSubject.complete();

      // THEN
      expect(eligibiliteServiceFormService.getEligibiliteService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eligibiliteServiceService.update).toHaveBeenCalledWith(expect.objectContaining(eligibiliteService));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEligibiliteService>();
      const eligibiliteService = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
      vi.spyOn(eligibiliteServiceFormService, 'getEligibiliteService').mockReturnValue({ id: null });
      vi.spyOn(eligibiliteServiceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eligibiliteService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(eligibiliteService);
      saveSubject.complete();

      // THEN
      expect(eligibiliteServiceFormService.getEligibiliteService).toHaveBeenCalled();
      expect(eligibiliteServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEligibiliteService>();
      const eligibiliteService = { id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' };
      vi.spyOn(eligibiliteServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eligibiliteService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eligibiliteServiceService.update).toHaveBeenCalled();
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

    describe('compareSegmentClient', () => {
      it('should forward to segmentClientService', () => {
        const entity = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
        const entity2 = { id: '3dfe0be4-098d-4b25-9305-36dbaeacd2d1' };
        vi.spyOn(segmentClientService, 'compareSegmentClient');
        comp.compareSegmentClient(entity, entity2);
        expect(segmentClientService.compareSegmentClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeClient', () => {
      it('should forward to typeClientService', () => {
        const entity = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
        const entity2 = { id: 'c3bcad5f-3b94-4cfe-b1a6-4c3b86b45e40' };
        vi.spyOn(typeClientService, 'compareTypeClient');
        comp.compareTypeClient(entity, entity2);
        expect(typeClientService.compareTypeClient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
