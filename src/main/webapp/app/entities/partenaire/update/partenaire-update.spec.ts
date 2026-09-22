import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartenaire } from '../partenaire.model';
import { PartenaireService } from '../service/partenaire.service';

import { PartenaireFormService } from './partenaire-form.service';
import { PartenaireUpdate } from './partenaire-update';

describe('Partenaire Management Update Component', () => {
  let comp: PartenaireUpdate;
  let fixture: ComponentFixture<PartenaireUpdate>;
  let activatedRoute: ActivatedRoute;
  let partenaireFormService: PartenaireFormService;
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

    fixture = TestBed.createComponent(PartenaireUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partenaireFormService = TestBed.inject(PartenaireFormService);
    partenaireService = TestBed.inject(PartenaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const partenaire: IPartenaire = { id: 'b6fc1546-6825-402d-9e00-2c8ab08f6246' };

      activatedRoute.data = of({ partenaire });
      comp.ngOnInit();

      expect(comp.partenaire).toEqual(partenaire);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartenaire>();
      const partenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      vi.spyOn(partenaireFormService, 'getPartenaire').mockReturnValue(partenaire);
      vi.spyOn(partenaireService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partenaire);
      saveSubject.complete();

      // THEN
      expect(partenaireFormService.getPartenaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partenaireService.update).toHaveBeenCalledWith(expect.objectContaining(partenaire));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartenaire>();
      const partenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      vi.spyOn(partenaireFormService, 'getPartenaire').mockReturnValue({ id: null });
      vi.spyOn(partenaireService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partenaire);
      saveSubject.complete();

      // THEN
      expect(partenaireFormService.getPartenaire).toHaveBeenCalled();
      expect(partenaireService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartenaire>();
      const partenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      vi.spyOn(partenaireService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partenaireService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
