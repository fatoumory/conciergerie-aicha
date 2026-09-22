import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { StatutDemandeService } from 'app/entities/statut-demande/service/statut-demande.service';
import { IStatutDemande } from 'app/entities/statut-demande/statut-demande.model';
import { IHistoriqueStatutDemande } from '../historique-statut-demande.model';
import { HistoriqueStatutDemandeService } from '../service/historique-statut-demande.service';

import { HistoriqueStatutDemandeFormService } from './historique-statut-demande-form.service';
import { HistoriqueStatutDemandeUpdate } from './historique-statut-demande-update';

describe('HistoriqueStatutDemande Management Update Component', () => {
  let comp: HistoriqueStatutDemandeUpdate;
  let fixture: ComponentFixture<HistoriqueStatutDemandeUpdate>;
  let activatedRoute: ActivatedRoute;
  let historiqueStatutDemandeFormService: HistoriqueStatutDemandeFormService;
  let historiqueStatutDemandeService: HistoriqueStatutDemandeService;
  let demandeService: DemandeService;
  let statutDemandeService: StatutDemandeService;

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

    fixture = TestBed.createComponent(HistoriqueStatutDemandeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historiqueStatutDemandeFormService = TestBed.inject(HistoriqueStatutDemandeFormService);
    historiqueStatutDemandeService = TestBed.inject(HistoriqueStatutDemandeService);
    demandeService = TestBed.inject(DemandeService);
    statutDemandeService = TestBed.inject(StatutDemandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Demande query and add missing value', () => {
      const historiqueStatutDemande: IHistoriqueStatutDemande = { id: '9ca55868-530a-48d4-9d50-356a345067a7' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      historiqueStatutDemande.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' }];
      vi.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const additionalDemandes = [demande];
      const expectedCollection: IDemande[] = [...additionalDemandes, ...demandeCollection];
      vi.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historiqueStatutDemande });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(
        demandeCollection,
        ...additionalDemandes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.demandesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call StatutDemande query and add missing value', () => {
      const historiqueStatutDemande: IHistoriqueStatutDemande = { id: '9ca55868-530a-48d4-9d50-356a345067a7' };
      const statut: IStatutDemande = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
      historiqueStatutDemande.statut = statut;

      const statutDemandeCollection: IStatutDemande[] = [{ id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' }];
      vi.spyOn(statutDemandeService, 'query').mockReturnValue(of(new HttpResponse({ body: statutDemandeCollection })));
      const additionalStatutDemandes = [statut];
      const expectedCollection: IStatutDemande[] = [...additionalStatutDemandes, ...statutDemandeCollection];
      vi.spyOn(statutDemandeService, 'addStatutDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historiqueStatutDemande });
      comp.ngOnInit();

      expect(statutDemandeService.query).toHaveBeenCalled();
      expect(statutDemandeService.addStatutDemandeToCollectionIfMissing).toHaveBeenCalledWith(
        statutDemandeCollection,
        ...additionalStatutDemandes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statutDemandesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const historiqueStatutDemande: IHistoriqueStatutDemande = { id: '9ca55868-530a-48d4-9d50-356a345067a7' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      historiqueStatutDemande.demande = demande;
      const statut: IStatutDemande = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
      historiqueStatutDemande.statut = statut;

      activatedRoute.data = of({ historiqueStatutDemande });
      comp.ngOnInit();

      expect(comp.demandesSharedCollection()).toContainEqual(demande);
      expect(comp.statutDemandesSharedCollection()).toContainEqual(statut);
      expect(comp.historiqueStatutDemande).toEqual(historiqueStatutDemande);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IHistoriqueStatutDemande>();
      const historiqueStatutDemande = { id: 'b034fefc-0610-4162-94d9-de92093e7063' };
      vi.spyOn(historiqueStatutDemandeFormService, 'getHistoriqueStatutDemande').mockReturnValue(historiqueStatutDemande);
      vi.spyOn(historiqueStatutDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueStatutDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(historiqueStatutDemande);
      saveSubject.complete();

      // THEN
      expect(historiqueStatutDemandeFormService.getHistoriqueStatutDemande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(historiqueStatutDemandeService.update).toHaveBeenCalledWith(expect.objectContaining(historiqueStatutDemande));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IHistoriqueStatutDemande>();
      const historiqueStatutDemande = { id: 'b034fefc-0610-4162-94d9-de92093e7063' };
      vi.spyOn(historiqueStatutDemandeFormService, 'getHistoriqueStatutDemande').mockReturnValue({ id: null });
      vi.spyOn(historiqueStatutDemandeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueStatutDemande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(historiqueStatutDemande);
      saveSubject.complete();

      // THEN
      expect(historiqueStatutDemandeFormService.getHistoriqueStatutDemande).toHaveBeenCalled();
      expect(historiqueStatutDemandeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IHistoriqueStatutDemande>();
      const historiqueStatutDemande = { id: 'b034fefc-0610-4162-94d9-de92093e7063' };
      vi.spyOn(historiqueStatutDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueStatutDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historiqueStatutDemandeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDemande', () => {
      it('should forward to demandeService', () => {
        const entity = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
        const entity2 = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
        vi.spyOn(demandeService, 'compareDemande');
        comp.compareDemande(entity, entity2);
        expect(demandeService.compareDemande).toHaveBeenCalledWith(entity, entity2);
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
  });
});
