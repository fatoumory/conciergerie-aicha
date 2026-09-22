import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { IEvaluation } from '../evaluation.model';
import { EvaluationService } from '../service/evaluation.service';

import { EvaluationFormService } from './evaluation-form.service';
import { EvaluationUpdate } from './evaluation-update';

describe('Evaluation Management Update Component', () => {
  let comp: EvaluationUpdate;
  let fixture: ComponentFixture<EvaluationUpdate>;
  let activatedRoute: ActivatedRoute;
  let evaluationFormService: EvaluationFormService;
  let evaluationService: EvaluationService;
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

    fixture = TestBed.createComponent(EvaluationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaluationFormService = TestBed.inject(EvaluationFormService);
    evaluationService = TestBed.inject(EvaluationService);
    demandeService = TestBed.inject(DemandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call demande query and add missing value', () => {
      const evaluation: IEvaluation = { id: '7d0d22b5-b171-4dc7-808e-6e92712c882e' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      evaluation.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' }];
      vi.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const expectedCollection: IDemande[] = [demande, ...demandeCollection];
      vi.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(demandeCollection, demande);
      expect(comp.demandesCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const evaluation: IEvaluation = { id: '7d0d22b5-b171-4dc7-808e-6e92712c882e' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      evaluation.demande = demande;

      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      expect(comp.demandesCollection()).toContainEqual(demande);
      expect(comp.evaluation).toEqual(evaluation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEvaluation>();
      const evaluation = { id: '637d3465-3a28-4513-8c01-e1294c65fab0' };
      vi.spyOn(evaluationFormService, 'getEvaluation').mockReturnValue(evaluation);
      vi.spyOn(evaluationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(evaluation);
      saveSubject.complete();

      // THEN
      expect(evaluationFormService.getEvaluation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaluationService.update).toHaveBeenCalledWith(expect.objectContaining(evaluation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEvaluation>();
      const evaluation = { id: '637d3465-3a28-4513-8c01-e1294c65fab0' };
      vi.spyOn(evaluationFormService, 'getEvaluation').mockReturnValue({ id: null });
      vi.spyOn(evaluationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(evaluation);
      saveSubject.complete();

      // THEN
      expect(evaluationFormService.getEvaluation).toHaveBeenCalled();
      expect(evaluationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEvaluation>();
      const evaluation = { id: '637d3465-3a28-4513-8c01-e1294c65fab0' };
      vi.spyOn(evaluationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaluationService.update).toHaveBeenCalled();
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
