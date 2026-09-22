import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { QuotaServiceDetail } from './quota-service-detail';

describe('QuotaService Management Detail Component', () => {
  let comp: QuotaServiceDetail;
  let fixture: ComponentFixture<QuotaServiceDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./quota-service-detail').then(m => m.QuotaServiceDetail),
              resolve: { quotaService: () => of({ id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' }) },
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
    fixture = TestBed.createComponent(QuotaServiceDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load quotaService on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuotaServiceDetail);

      // THEN
      expect(instance.quotaService()).toEqual(expect.objectContaining({ id: 'daade9e5-0205-4f17-8182-5b2a4757eae2' }));
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
