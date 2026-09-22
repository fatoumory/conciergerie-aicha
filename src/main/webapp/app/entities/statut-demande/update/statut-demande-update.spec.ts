import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { StatutDemandeService } from '../service/statut-demande.service';
import { IStatutDemande } from '../statut-demande.model';

import { StatutDemandeFormService } from './statut-demande-form.service';
import { StatutDemandeUpdate } from './statut-demande-update';

describe('StatutDemande Management Update Component', () => {
  let comp: StatutDemandeUpdate;
  let fixture: ComponentFixture<StatutDemandeUpdate>;
  let activatedRoute: ActivatedRoute;
  let statutDemandeFormService: StatutDemandeFormService;
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

    fixture = TestBed.createComponent(StatutDemandeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statutDemandeFormService = TestBed.inject(StatutDemandeFormService);
    statutDemandeService = TestBed.inject(StatutDemandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const statutDemande: IStatutDemande = { id: 'e8257824-8fba-4b3e-a113-7cad10551751' };

      activatedRoute.data = of({ statutDemande });
      comp.ngOnInit();

      expect(comp.statutDemande).toEqual(statutDemande);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatutDemande>();
      const statutDemande = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
      vi.spyOn(statutDemandeFormService, 'getStatutDemande').mockReturnValue(statutDemande);
      vi.spyOn(statutDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statutDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(statutDemande);
      saveSubject.complete();

      // THEN
      expect(statutDemandeFormService.getStatutDemande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statutDemandeService.update).toHaveBeenCalledWith(expect.objectContaining(statutDemande));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatutDemande>();
      const statutDemande = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
      vi.spyOn(statutDemandeFormService, 'getStatutDemande').mockReturnValue({ id: null });
      vi.spyOn(statutDemandeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statutDemande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(statutDemande);
      saveSubject.complete();

      // THEN
      expect(statutDemandeFormService.getStatutDemande).toHaveBeenCalled();
      expect(statutDemandeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IStatutDemande>();
      const statutDemande = { id: 'b7d3c4fe-987a-46a6-9081-2c6387b7e3e8' };
      vi.spyOn(statutDemandeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statutDemande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statutDemandeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
