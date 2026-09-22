import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { FactureDetail } from './facture-detail';

describe('Facture Management Detail Component', () => {
  let comp: FactureDetail;
  let fixture: ComponentFixture<FactureDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./facture-detail').then(m => m.FactureDetail),
              resolve: { facture: () => of({ id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' }) },
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
    fixture = TestBed.createComponent(FactureDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load facture on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactureDetail);

      // THEN
      expect(instance.facture()).toEqual(expect.objectContaining({ id: 'eecaf7bf-3d63-4019-a2f9-e9df461bc1c5' }));
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
