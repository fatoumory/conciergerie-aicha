import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { ICodePromo } from 'app/entities/code-promo/code-promo.model';
import { CodePromoService } from 'app/entities/code-promo/service/code-promo.service';
import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { StatutDemandeService } from 'app/entities/statut-demande/service/statut-demande.service';
import { IStatutDemande } from 'app/entities/statut-demande/statut-demande.model';
import { TypeDemandeService } from 'app/entities/type-demande/service/type-demande.service';
import { ITypeDemande } from 'app/entities/type-demande/type-demande.model';
import { IDemande } from '../demande.model';
import { DemandeService } from '../service/demande.service';

import { DemandeFormService } from './demande-form.service';
import { DemandeUpdate } from './demande-update';

describe('Demande Management Update Component', () => {
  let comp: DemandeUpdate;
  let fixture: ComponentFixture<DemandeUpdate>;
  let activatedRoute: ActivatedRoute;
  let demandeFormService: DemandeFormService;
  let demandeService: DemandeService;
  let clientService: ClientService;
  let serviceConciergerieService: ServiceConciergerieService;
  let typeDemandeService: TypeDemandeService;
  let statutDemandeService: StatutDemandeService;
  let codePromoService: CodePromoService;

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

    fixture = TestBed.createComponent(DemandeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeFormService = TestBed.inject(DemandeFormService);
    demandeService = TestBed.inject(DemandeService);
    clientService = TestBed.inject(ClientService);
    serviceConciergerieService = TestBed.inject(ServiceConciergerieService);
    typeDemandeService = TestBed.inject(TypeDemandeService);
    statutDemandeService = TestBed.inject(StatutDemandeService);
    codePromoService = TestBed.inject(CodePromoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Client query and add missing value', () => {
      const demande: IDemande = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      demande.client = client;

      const clientCollection: IClient[] = [{ id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' }];
      vi.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      vi.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.clientsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ServiceConciergerie query and add missing value', () => {
      const demande: IDemande = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      demande.service = service;

      const serviceConciergerieCollection: IServiceConciergerie[] = [{ id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' }];
      vi.spyOn(serviceConciergerieService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceConciergerieCollection })));
      const additionalServiceConciergeries = [service];
      const expectedCollection: IServiceConciergerie[] = [...additionalServiceConciergeries, ...serviceConciergerieCollection];
      vi.spyOn(serviceConciergerieService, 'addServiceConciergerieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(serviceConciergerieService.query).toHaveBeenCalled();
      expect(serviceConciergerieService.addServiceConciergerieToCollectionIfMissing).toHaveBeenCalledWith(
        serviceConciergerieCollection,
        ...additionalServiceConciergeries.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviceConciergeriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TypeDemande query and add missing value', () => {
      const demande: IDemande = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
      const typeDemande: ITypeDemande = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
      demande.typeDemande = typeDemande;

      const typeDemandeCollection: ITypeDemande[] = [{ id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' }];
      vi.spyOn(typeDemandeService, 'query').mockReturnValue(of(new HttpResponse({ body: typeDemandeCollection })));
      const additionalTypeDemandes = [typeDemande];
      const expectedCollection: ITypeDemande[] = [...additionalTypeDemandes, ...typeDemandeCollection];
      vi.spyOn(typeDemandeService, 'addTypeDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(typeDemandeService.query).toHaveBeenCalled();
      expect(typeDemandeService.addTypeDemandeToCollectionIfMissing).toHaveBeenCalledWith(
        typeDemandeCollection,
        ...additionalTypeDemandes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.typeDemandesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call StatutDemande query and add missing value', () => {
      const demande: IDemande = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
      const statut: IStatutDemande = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
      demande.statut = statut;

      const statutDemandeCollection: IStatutDemande[] = [{ id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' }];
      vi.spyOn(statutDemandeService, 'query').mockReturnValue(of(new HttpResponse({ body: statutDemandeCollection })));
      const additionalStatutDemandes = [statut];
      const expectedCollection: IStatutDemande[] = [...additionalStatutDemandes, ...statutDemandeCollection];
      vi.spyOn(statutDemandeService, 'addStatutDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(statutDemandeService.query).toHaveBeenCalled();
      expect(statutDemandeService.addStatutDemandeToCollectionIfMissing).toHaveBeenCalledWith(
        statutDemandeCollection,
        ...additionalStatutDemandes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statutDemandesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CodePromo query and add missing value', () => {
      const demande: IDemande = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
      const codePromo: ICodePromo = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
      demande.codePromo = codePromo;

      const codePromoCollection: ICodePromo[] = [{ id: 'b727eb78-e176-4142-8685-21300126e80d' }];
      vi.spyOn(codePromoService, 'query').mockReturnValue(of(new HttpResponse({ body: codePromoCollection })));
      const additionalCodePromos = [codePromo];
      const expectedCollection: ICodePromo[] = [...additionalCodePromos, ...codePromoCollection];
      vi.spyOn(codePromoService, 'addCodePromoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(codePromoService.query).toHaveBeenCalled();
      expect(codePromoService.addCodePromoToCollectionIfMissing).toHaveBeenCalledWith(
        codePromoCollection,
        ...additionalCodePromos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.codePromosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const demande: IDemande = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
      const client: IClient = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      demande.client = client;
      const service: IServiceConciergerie = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
      demande.service = service;
      const typeDemande: ITypeDemande = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
      demande.typeDemande = typeDemande;
      const statut: IStatutDemande = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
      demande.statut = statut;
      const codePromo: ICodePromo = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
      demande.codePromo = codePromo;

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(comp.clientsSharedCollection()).toContainEqual(client);
      expect(comp.serviceConciergeriesSharedCollection()).toContainEqual(service);
      expect(comp.typeDemandesSharedCollection()).toContainEqual(typeDemande);
      expect(comp.statutDemandesSharedCollection()).toContainEqual(statut);
      expect(comp.codePromosSharedCollection()).toContainEqual(codePromo);
      expect(comp.demande).toEqual(demande);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDemande>();
      const demande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      vi.spyOn(demandeFormService, 'getDemande').mockReturnValue(demande);
      vi.spyOn(demandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(demande);
      saveSubject.complete();

      // THEN
      expect(demandeFormService.getDemande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeService.update).toHaveBeenCalledWith(expect.objectContaining(demande));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDemande>();
      const demande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      vi.spyOn(demandeFormService, 'getDemande').mockReturnValue({ id: null });
      vi.spyOn(demandeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(demande);
      saveSubject.complete();

      // THEN
      expect(demandeFormService.getDemande).toHaveBeenCalled();
      expect(demandeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDemande>();
      const demande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      vi.spyOn(demandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeService.update).toHaveBeenCalled();
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

    describe('compareServiceConciergerie', () => {
      it('should forward to serviceConciergerieService', () => {
        const entity = { id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' };
        const entity2 = { id: 'c043c152-745c-4486-b5a8-711daf859c96' };
        vi.spyOn(serviceConciergerieService, 'compareServiceConciergerie');
        comp.compareServiceConciergerie(entity, entity2);
        expect(serviceConciergerieService.compareServiceConciergerie).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeDemande', () => {
      it('should forward to typeDemandeService', () => {
        const entity = { id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' };
        const entity2 = { id: 'b3d6689f-dd1c-49f0-b65c-9fee5dabd02e' };
        vi.spyOn(typeDemandeService, 'compareTypeDemande');
        comp.compareTypeDemande(entity, entity2);
        expect(typeDemandeService.compareTypeDemande).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatutDemande', () => {
      it('should forward to statutDemandeService', () => {
        const entity = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
        const entity2 = { id: 'e8257824-8fba-4b3e-a113-7cad10551751' };
        vi.spyOn(statutDemandeService, 'compareStatutDemande');
        comp.compareStatutDemande(entity, entity2);
        expect(statutDemandeService.compareStatutDemande).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCodePromo', () => {
      it('should forward to codePromoService', () => {
        const entity = { id: 'b727eb78-e176-4142-8685-21300126e80d' };
        const entity2 = { id: '2f309410-f594-41bc-8818-5fbfb8f44a29' };
        vi.spyOn(codePromoService, 'compareCodePromo');
        comp.compareCodePromo(entity, entity2);
        expect(codePromoService.compareCodePromo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
