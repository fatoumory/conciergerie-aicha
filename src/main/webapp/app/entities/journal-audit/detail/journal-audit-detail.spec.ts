import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { JournalAuditDetail } from './journal-audit-detail';

describe('JournalAudit Management Detail Component', () => {
  let comp: JournalAuditDetail;
  let fixture: ComponentFixture<JournalAuditDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./journal-audit-detail').then(m => m.JournalAuditDetail),
              resolve: { journalAudit: () => of({ id: 'c6f55784-881d-4e0d-a58a-399582a89830' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JournalAuditDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load journalAudit on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', JournalAuditDetail);

      // THEN
      expect(instance.journalAudit()).toEqual(expect.objectContaining({ id: 'c6f55784-881d-4e0d-a58a-399582a89830' }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
