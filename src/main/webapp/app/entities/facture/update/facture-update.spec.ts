import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { IFacture } from '../facture.model';
import { FactureService } from '../service/facture.service';

import { FactureFormService } from './facture-form.service';
import { FactureUpdate } from './facture-update';

describe('Facture Management Update Component', () => {
  let comp: FactureUpdate;
  let fixture: ComponentFixture<FactureUpdate>;
  let activatedRoute: ActivatedRoute;
  let factureFormService: FactureFormService;
  let factureService: FactureService;
  let demandeService: DemandeService;

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

    fixture = TestBed.createComponent(FactureUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factureFormService = TestBed.inject(FactureFormService);
    factureService = TestBed.inject(FactureService);
    demandeService = TestBed.inject(DemandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call demande query and add missing value', () => {
      const facture: IFacture = { id: '51300b25-7f46-437f-9213-a92091e89ea4' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      facture.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' }];
      vi.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const expectedCollection: IDemande[] = [demande, ...demandeCollection];
      vi.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(demandeCollection, demande);
      expect(comp.demandesCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facture: IFacture = { id: '51300b25-7f46-437f-9213-a92091e89ea4' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      facture.demande = demande;

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(comp.demandesCollection()).toContainEqual(demande);
      expect(comp.facture).toEqual(facture);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacture>();
      const facture = { id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' };
      vi.spyOn(factureFormService, 'getFacture').mockReturnValue(facture);
      vi.spyOn(factureService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facture);
      saveSubject.complete();

      // THEN
      expect(factureFormService.getFacture).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factureService.update).toHaveBeenCalledWith(expect.objectContaining(facture));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacture>();
      const facture = { id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' };
      vi.spyOn(factureFormService, 'getFacture').mockReturnValue({ id: null });
      vi.spyOn(factureService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facture);
      saveSubject.complete();

      // THEN
      expect(factureFormService.getFacture).toHaveBeenCalled();
      expect(factureService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacture>();
      const facture = { id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' };
      vi.spyOn(factureService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factureService.update).toHaveBeenCalled();
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
  });
});
