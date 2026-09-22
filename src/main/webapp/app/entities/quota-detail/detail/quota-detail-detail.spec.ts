import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { QuotaDetailDetail } from './quota-detail-detail';

describe('QuotaDetail Management Detail Component', () => {
  let comp: QuotaDetailDetail;
  let fixture: ComponentFixture<QuotaDetailDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./quota-detail-detail').then(m => m.QuotaDetailDetail),
              resolve: { quotaDetail: () => of({ id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' }) },
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
    fixture = TestBed.createComponent(QuotaDetailDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load quotaDetail on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuotaDetailDetail);

      // THEN
      expect(instance.quotaDetail()).toEqual(expect.objectContaining({ id: '31462f49-bad8-4eb5-a5b5-d2493b87b976' }));
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
