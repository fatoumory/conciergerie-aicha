import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ConsommationQuotaDetail } from './consommation-quota-detail';

describe('ConsommationQuota Management Detail Component', () => {
  let comp: ConsommationQuotaDetail;
  let fixture: ComponentFixture<ConsommationQuotaDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./consommation-quota-detail').then(m => m.ConsommationQuotaDetail),
              resolve: { consommationQuota: () => of({ id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' }) },
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
    fixture = TestBed.createComponent(ConsommationQuotaDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load consommationQuota on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConsommationQuotaDetail);

      // THEN
      expect(instance.consommationQuota()).toEqual(expect.objectContaining({ id: 'dc203995-2178-4c4a-94e1-cc7c80ee7be6' }));
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
