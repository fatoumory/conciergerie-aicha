import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { ICodeQrService } from '../code-qr-service.model';
import { CodeQrServiceService } from '../service/code-qr-service.service';

import { CodeQrServiceFormService } from './code-qr-service-form.service';
import { CodeQrServiceUpdate } from './code-qr-service-update';

describe('CodeQrService Management Update Component', () => {
  let comp: CodeQrServiceUpdate;
  let fixture: ComponentFixture<CodeQrServiceUpdate>;
  let activatedRoute: ActivatedRoute;
  let codeQrServiceFormService: CodeQrServiceFormService;
  let codeQrServiceService: CodeQrServiceService;
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

    fixture = TestBed.createComponent(CodeQrServiceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codeQrServiceFormService = TestBed.inject(CodeQrServiceFormService);
    codeQrServiceService = TestBed.inject(CodeQrServiceService);
    demandeService = TestBed.inject(DemandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call demande query and add missing value', () => {
      const codeQrService: ICodeQrService = { id: 'd244b8ab-9fc3-43a7-88ad-33f0a5709573' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      codeQrService.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' }];
      vi.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const expectedCollection: IDemande[] = [demande, ...demandeCollection];
      vi.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ codeQrService });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(demandeCollection, demande);
      expect(comp.demandesCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const codeQrService: ICodeQrService = { id: 'd244b8ab-9fc3-43a7-88ad-33f0a5709573' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      codeQrService.demande = demande;

      activatedRoute.data = of({ codeQrService });
      comp.ngOnInit();

      expect(comp.demandesCollection()).toContainEqual(demande);
      expect(comp.codeQrService).toEqual(codeQrService);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICodeQrService>();
      const codeQrService = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
      vi.spyOn(codeQrServiceFormService, 'getCodeQrService').mockReturnValue(codeQrService);
      vi.spyOn(codeQrServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeQrService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(codeQrService);
      saveSubject.complete();

      // THEN
      expect(codeQrServiceFormService.getCodeQrService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(codeQrServiceService.update).toHaveBeenCalledWith(expect.objectContaining(codeQrService));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICodeQrService>();
      const codeQrService = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
      vi.spyOn(codeQrServiceFormService, 'getCodeQrService').mockReturnValue({ id: null });
      vi.spyOn(codeQrServiceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeQrService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(codeQrService);
      saveSubject.complete();

      // THEN
      expect(codeQrServiceFormService.getCodeQrService).toHaveBeenCalled();
      expect(codeQrServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICodeQrService>();
      const codeQrService = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
      vi.spyOn(codeQrServiceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeQrService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codeQrServiceService.update).toHaveBeenCalled();
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
