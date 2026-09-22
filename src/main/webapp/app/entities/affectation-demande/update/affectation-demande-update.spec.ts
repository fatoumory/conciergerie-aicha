import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { IAffectationDemande } from '../affectation-demande.model';
import { AffectationDemandeService } from '../service/affectation-demande.service';

import { AffectationDemandeFormService } from './affectation-demande-form.service';
import { AffectationDemandeUpdate } from './affectation-demande-update';

describe('AffectationDemande Management Update Component', () => {
  let comp: AffectationDemandeUpdate;
  let fixture: ComponentFixture<AffectationDemandeUpdate>;
  let activatedRoute: ActivatedRoute;
  let affectationDemandeFormService: AffectationDemandeFormService;
  let affectationDemandeService: AffectationDemandeService;
  let demandeService: DemandeService;
  let partenaireService: PartenaireService;

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

    fixture = TestBed.createComponent(AffectationDemandeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    affectationDemandeFormService = TestBed.inject(AffectationDemandeFormService);
    affectationDemandeService = TestBed.inject(AffectationDemandeService);
    demandeService = TestBed.inject(DemandeService);
    partenaireService = TestBed.inject(PartenaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Demande query and add missing value', () => {
      const affectationDemande: IAffectationDemande = { id: '01609fa9-9d4c-41c5-974f-1dc953bb53c0' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      affectationDemande.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' }];
      vi.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const additionalDemandes = [demande];
      const expectedCollection: IDemande[] = [...additionalDemandes, ...demandeCollection];
      vi.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ affectationDemande });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(
        demandeCollection,
        ...additionalDemandes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.demandesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Partenaire query and add missing value', () => {
      const affectationDemande: IAffectationDemande = { id: '01609fa9-9d4c-41c5-974f-1dc953bb53c0' };
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      affectationDemande.partenaire = partenaire;

      const partenaireCollection: IPartenaire[] = [{ id: 'd330918e-c880-4b4f-a4ba-30a19287c322' }];
      vi.spyOn(partenaireService, 'query').mockReturnValue(of(new HttpResponse({ body: partenaireCollection })));
      const additionalPartenaires = [partenaire];
      const expectedCollection: IPartenaire[] = [...additionalPartenaires, ...partenaireCollection];
      vi.spyOn(partenaireService, 'addPartenaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ affectationDemande });
      comp.ngOnInit();

      expect(partenaireService.query).toHaveBeenCalled();
      expect(partenaireService.addPartenaireToCollectionIfMissing).toHaveBeenCalledWith(
        partenaireCollection,
        ...additionalPartenaires.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partenairesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const affectationDemande: IAffectationDemande = { id: '01609fa9-9d4c-41c5-974f-1dc953bb53c0' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      affectationDemande.demande = demande;
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      affectationDemande.partenaire = partenaire;

      activatedRoute.data = of({ affectationDemande });
      comp.ngOnInit();

      expect(comp.demandesSharedCollection()).toContainEqual(demande);
      expect(comp.partenairesSharedCollection()).toContainEqual(partenaire);
      expect(comp.affectationDemande).toEqual(affectationDemande);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectationDemande>();
      const affectationDemande = { id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' };
      vi.spyOn(affectationDemandeFormService, 'getAffectationDemande').mockReturnValue(affectationDemande);
      vi.spyOn(affectationDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectationDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectationDemande);
      saveSubject.complete();

      // THEN
      expect(affectationDemandeFormService.getAffectationDemande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(affectationDemandeService.update).toHaveBeenCalledWith(expect.objectContaining(affectationDemande));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectationDemande>();
      const affectationDemande = { id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' };
      vi.spyOn(affectationDemandeFormService, 'getAffectationDemande').mockReturnValue({ id: null });
      vi.spyOn(affectationDemandeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectationDemande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectationDemande);
      saveSubject.complete();

      // THEN
      expect(affectationDemandeFormService.getAffectationDemande).toHaveBeenCalled();
      expect(affectationDemandeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectationDemande>();
      const affectationDemande = { id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' };
      vi.spyOn(affectationDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectationDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(affectationDemandeService.update).toHaveBeenCalled();
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

    describe('comparePartenaire', () => {
      it('should forward to partenaireService', () => {
        const entity = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
        const entity2 = { id: 'b6fc1546-6825-402d-9e00-2c8ab08f6246' };
        vi.spyOn(partenaireService, 'comparePartenaire');
        comp.comparePartenaire(entity, entity2);
        expect(partenaireService.comparePartenaire).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
