import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CompteStockDetail } from './compte-stock-detail';

describe('CompteStock Management Detail Component', () => {
  let comp: CompteStockDetail;
  let fixture: ComponentFixture<CompteStockDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./compte-stock-detail').then(m => m.CompteStockDetail),
              resolve: { compteStock: () => of({ id: '51dcb663-66e6-485f-ad6e-94371c61b67e' }) },
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
    fixture = TestBed.createComponent(CompteStockDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load compteStock on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CompteStockDetail);

      // THEN
      expect(instance.compteStock()).toEqual(expect.objectContaining({ id: '51dcb663-66e6-485f-ad6e-94371c61b67e' }));
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
