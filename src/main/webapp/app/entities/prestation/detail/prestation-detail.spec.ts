import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PrestationDetail } from './prestation-detail';

describe('Prestation Management Detail Component', () => {
  let comp: PrestationDetail;
  let fixture: ComponentFixture<PrestationDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./prestation-detail').then(m => m.PrestationDetail),
              resolve: { prestation: () => of({ id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' }) },
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
    fixture = TestBed.createComponent(PrestationDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load prestation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PrestationDetail);

      // THEN
      expect(instance.prestation()).toEqual(expect.objectContaining({ id: '9bb983be-2cce-4c48-8460-9c16a43baaaf' }));
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
