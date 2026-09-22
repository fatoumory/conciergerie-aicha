import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TypeDemandeDetail } from './type-demande-detail';

describe('TypeDemande Management Detail Component', () => {
  let comp: TypeDemandeDetail;
  let fixture: ComponentFixture<TypeDemandeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./type-demande-detail').then(m => m.TypeDemandeDetail),
              resolve: { typeDemande: () => of({ id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' }) },
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
    fixture = TestBed.createComponent(TypeDemandeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load typeDemande on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeDemandeDetail);

      // THEN
      expect(instance.typeDemande()).toEqual(expect.objectContaining({ id: '3bce589b-05cc-4b45-95d3-6d0dcb5e23ee' }));
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
