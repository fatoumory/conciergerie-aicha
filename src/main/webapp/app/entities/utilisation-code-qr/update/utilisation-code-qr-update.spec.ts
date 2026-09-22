import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICodeQrService } from 'app/entities/code-qr-service/code-qr-service.model';
import { CodeQrServiceService } from 'app/entities/code-qr-service/service/code-qr-service.service';
import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { UtilisationCodeQrService } from '../service/utilisation-code-qr.service';
import { IUtilisationCodeQr } from '../utilisation-code-qr.model';

import { UtilisationCodeQrFormService } from './utilisation-code-qr-form.service';
import { UtilisationCodeQrUpdate } from './utilisation-code-qr-update';

describe('UtilisationCodeQr Management Update Component', () => {
  let comp: UtilisationCodeQrUpdate;
  let fixture: ComponentFixture<UtilisationCodeQrUpdate>;
  let activatedRoute: ActivatedRoute;
  let utilisationCodeQrFormService: UtilisationCodeQrFormService;
  let utilisationCodeQrService: UtilisationCodeQrService;
  let codeQrServiceService: CodeQrServiceService;
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

    fixture = TestBed.createComponent(UtilisationCodeQrUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilisationCodeQrFormService = TestBed.inject(UtilisationCodeQrFormService);
    utilisationCodeQrService = TestBed.inject(UtilisationCodeQrService);
    codeQrServiceService = TestBed.inject(CodeQrServiceService);
    partenaireService = TestBed.inject(PartenaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CodeQrService query and add missing value', () => {
      const utilisationCodeQr: IUtilisationCodeQr = { id: '6cbdc051-f287-4d20-85bd-683ae43a902a' };
      const codeQrService: ICodeQrService = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
      utilisationCodeQr.codeQrService = codeQrService;

      const codeQrServiceCollection: ICodeQrService[] = [{ id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' }];
      vi.spyOn(codeQrServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: codeQrServiceCollection })));
      const additionalCodeQrServices = [codeQrService];
      const expectedCollection: ICodeQrService[] = [...additionalCodeQrServices, ...codeQrServiceCollection];
      vi.spyOn(codeQrServiceService, 'addCodeQrServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisationCodeQr });
      comp.ngOnInit();

      expect(codeQrServiceService.query).toHaveBeenCalled();
      expect(codeQrServiceService.addCodeQrServiceToCollectionIfMissing).toHaveBeenCalledWith(
        codeQrServiceCollection,
        ...additionalCodeQrServices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.codeQrServicesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Partenaire query and add missing value', () => {
      const utilisationCodeQr: IUtilisationCodeQr = { id: '6cbdc051-f287-4d20-85bd-683ae43a902a' };
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      utilisationCodeQr.partenaire = partenaire;

      const partenaireCollection: IPartenaire[] = [{ id: 'd330918e-c880-4b4f-a4ba-30a19287c322' }];
      vi.spyOn(partenaireService, 'query').mockReturnValue(of(new HttpResponse({ body: partenaireCollection })));
      const additionalPartenaires = [partenaire];
      const expectedCollection: IPartenaire[] = [...additionalPartenaires, ...partenaireCollection];
      vi.spyOn(partenaireService, 'addPartenaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisationCodeQr });
      comp.ngOnInit();

      expect(partenaireService.query).toHaveBeenCalled();
      expect(partenaireService.addPartenaireToCollectionIfMissing).toHaveBeenCalledWith(
        partenaireCollection,
        ...additionalPartenaires.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partenairesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const utilisationCodeQr: IUtilisationCodeQr = { id: '6cbdc051-f287-4d20-85bd-683ae43a902a' };
      const codeQrService: ICodeQrService = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
      utilisationCodeQr.codeQrService = codeQrService;
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      utilisationCodeQr.partenaire = partenaire;

      activatedRoute.data = of({ utilisationCodeQr });
      comp.ngOnInit();

      expect(comp.codeQrServicesSharedCollection()).toContainEqual(codeQrService);
      expect(comp.partenairesSharedCollection()).toContainEqual(partenaire);
      expect(comp.utilisationCodeQr).toEqual(utilisationCodeQr);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUtilisationCodeQr>();
      const utilisationCodeQr = { id: '9609ad2a-cecd-47c9-a80a-730574aa5903' };
      vi.spyOn(utilisationCodeQrFormService, 'getUtilisationCodeQr').mockReturnValue(utilisationCodeQr);
      vi.spyOn(utilisationCodeQrService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisationCodeQr });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(utilisationCodeQr);
      saveSubject.complete();

      // THEN
      expect(utilisationCodeQrFormService.getUtilisationCodeQr).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilisationCodeQrService.update).toHaveBeenCalledWith(expect.objectContaining(utilisationCodeQr));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUtilisationCodeQr>();
      const utilisationCodeQr = { id: '9609ad2a-cecd-47c9-a80a-730574aa5903' };
      vi.spyOn(utilisationCodeQrFormService, 'getUtilisationCodeQr').mockReturnValue({ id: null });
      vi.spyOn(utilisationCodeQrService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisationCodeQr: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(utilisationCodeQr);
      saveSubject.complete();

      // THEN
      expect(utilisationCodeQrFormService.getUtilisationCodeQr).toHaveBeenCalled();
      expect(utilisationCodeQrService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IUtilisationCodeQr>();
      const utilisationCodeQr = { id: '9609ad2a-cecd-47c9-a80a-730574aa5903' };
      vi.spyOn(utilisationCodeQrService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisationCodeQr });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilisationCodeQrService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCodeQrService', () => {
      it('should forward to codeQrServiceService', () => {
        const entity = { id: '22ef53f8-4fa3-48b7-9f16-be67fa41d8c0' };
        const entity2 = { id: 'd244b8ab-9fc3-43a7-88ad-33f0a5709573' };
        vi.spyOn(codeQrServiceService, 'compareCodeQrService');
        comp.compareCodeQrService(entity, entity2);
        expect(codeQrServiceService.compareCodeQrService).toHaveBeenCalledWith(entity, entity2);
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
