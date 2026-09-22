import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { MouvementStockDetail } from './mouvement-stock-detail';

describe('MouvementStock Management Detail Component', () => {
  let comp: MouvementStockDetail;
  let fixture: ComponentFixture<MouvementStockDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./mouvement-stock-detail').then(m => m.MouvementStockDetail),
              resolve: { mouvementStock: () => of({ id: 'fc5bd5ed-2dc4-439b-bdbd-8c3315d903f7' }) },
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
    fixture = TestBed.createComponent(MouvementStockDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load mouvementStock on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MouvementStockDetail);

      // THEN
      expect(instance.mouvementStock()).toEqual(expect.objectContaining({ id: 'fc5bd5ed-2dc4-439b-bdbd-8c3315d903f7' }));
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
