import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { AffectationDemandeDetail } from './affectation-demande-detail';

describe('AffectationDemande Management Detail Component', () => {
  let comp: AffectationDemandeDetail;
  let fixture: ComponentFixture<AffectationDemandeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./affectation-demande-detail').then(m => m.AffectationDemandeDetail),
              resolve: { affectationDemande: () => of({ id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' }) },
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
    fixture = TestBed.createComponent(AffectationDemandeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load affectationDemande on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AffectationDemandeDetail);

      // THEN
      expect(instance.affectationDemande()).toEqual(expect.objectContaining({ id: 'd5ec25ab-63b7-4dd6-8063-6ca61783ff97' }));
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
