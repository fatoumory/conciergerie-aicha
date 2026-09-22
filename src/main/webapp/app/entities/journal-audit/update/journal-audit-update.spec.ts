import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IJournalAudit } from '../journal-audit.model';
import { JournalAuditService } from '../service/journal-audit.service';

import { JournalAuditFormService } from './journal-audit-form.service';
import { JournalAuditUpdate } from './journal-audit-update';

describe('JournalAudit Management Update Component', () => {
  let comp: JournalAuditUpdate;
  let fixture: ComponentFixture<JournalAuditUpdate>;
  let activatedRoute: ActivatedRoute;
  let journalAuditFormService: JournalAuditFormService;
  let journalAuditService: JournalAuditService;

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

    fixture = TestBed.createComponent(JournalAuditUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    journalAuditFormService = TestBed.inject(JournalAuditFormService);
    journalAuditService = TestBed.inject(JournalAuditService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const journalAudit: IJournalAudit = { id: 'e38a82e3-6545-408c-9dca-c2b98fb2af68' };

      activatedRoute.data = of({ journalAudit });
      comp.ngOnInit();

      expect(comp.journalAudit).toEqual(journalAudit);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IJournalAudit>();
      const journalAudit = { id: 'c6f55784-881d-4e0d-a58a-399582a89830' };
      vi.spyOn(journalAuditFormService, 'getJournalAudit').mockReturnValue(journalAudit);
      vi.spyOn(journalAuditService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journalAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(journalAudit);
      saveSubject.complete();

      // THEN
      expect(journalAuditFormService.getJournalAudit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(journalAuditService.update).toHaveBeenCalledWith(expect.objectContaining(journalAudit));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IJournalAudit>();
      const journalAudit = { id: 'c6f55784-881d-4e0d-a58a-399582a89830' };
      vi.spyOn(journalAuditFormService, 'getJournalAudit').mockReturnValue({ id: null });
      vi.spyOn(journalAuditService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journalAudit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(journalAudit);
      saveSubject.complete();

      // THEN
      expect(journalAuditFormService.getJournalAudit).toHaveBeenCalled();
      expect(journalAuditService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IJournalAudit>();
      const journalAudit = { id: 'c6f55784-881d-4e0d-a58a-399582a89830' };
      vi.spyOn(journalAuditService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ journalAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(journalAuditService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
