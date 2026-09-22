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
import { IPrestation } from '../prestation.model';
import { PrestationService } from '../service/prestation.service';

import { PrestationFormService } from './prestation-form.service';
import { PrestationUpdate } from './prestation-update';

describe('Prestation Management Update Component', () => {
  let comp: PrestationUpdate;
  let fixture: ComponentFixture<PrestationUpdate>;
  let activatedRoute: ActivatedRoute;
  let prestationFormService: PrestationFormService;
  let prestationService: PrestationService;
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

    fixture = TestBed.createComponent(PrestationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prestationFormService = TestBed.inject(PrestationFormService);
    prestationService = TestBed.inject(PrestationService);
    demandeService = TestBed.inject(DemandeService);
    partenaireService = TestBed.inject(PartenaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call demande query and add missing value', () => {
      const prestation: IPrestation = { id: '53ada915-c0bd-45c2-9fc0-60bd5169f1bd' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      prestation.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' }];
      vi.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const expectedCollection: IDemande[] = [demande, ...demandeCollection];
      vi.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestation });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(demandeCollection, demande);
      expect(comp.demandesCollection()).toEqual(expectedCollection);
    });

    it('should call Partenaire query and add missing value', () => {
      const prestation: IPrestation = { id: '53ada915-c0bd-45c2-9fc0-60bd5169f1bd' };
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      prestation.partenaire = partenaire;

      const partenaireCollection: IPartenaire[] = [{ id: 'd330918e-c880-4b4f-a4ba-30a19287c322' }];
      vi.spyOn(partenaireService, 'query').mockReturnValue(of(new HttpResponse({ body: partenaireCollection })));
      const additionalPartenaires = [partenaire];
      const expectedCollection: IPartenaire[] = [...additionalPartenaires, ...partenaireCollection];
      vi.spyOn(partenaireService, 'addPartenaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestation });
      comp.ngOnInit();

      expect(partenaireService.query).toHaveBeenCalled();
      expect(partenaireService.addPartenaireToCollectionIfMissing).toHaveBeenCalledWith(
        partenaireCollection,
        ...additionalPartenaires.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partenairesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const prestation: IPrestation = { id: '53ada915-c0bd-45c2-9fc0-60bd5169f1bd' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      prestation.demande = demande;
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      prestation.partenaire = partenaire;

      activatedRoute.data = of({ prestation });
      comp.ngOnInit();

      expect(comp.demandesCollection()).toContainEqual(demande);
      expect(comp.partenairesSharedCollection()).toContainEqual(partenaire);
      expect(comp.prestation).toEqual(prestation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPrestation>();
      const prestation = { id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' };
      vi.spyOn(prestationFormService, 'getPrestation').mockReturnValue(prestation);
      vi.spyOn(prestationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(prestation);
      saveSubject.complete();

      // THEN
      expect(prestationFormService.getPrestation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prestationService.update).toHaveBeenCalledWith(expect.objectContaining(prestation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPrestation>();
      const prestation = { id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' };
      vi.spyOn(prestationFormService, 'getPrestation').mockReturnValue({ id: null });
      vi.spyOn(prestationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(prestation);
      saveSubject.complete();

      // THEN
      expect(prestationFormService.getPrestation).toHaveBeenCalled();
      expect(prestationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPrestation>();
      const prestation = { id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' };
      vi.spyOn(prestationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prestationService.update).toHaveBeenCalled();
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
