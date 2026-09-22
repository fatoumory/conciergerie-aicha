import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CouverturePartenaireDetail } from './couverture-partenaire-detail';

describe('CouverturePartenaire Management Detail Component', () => {
  let comp: CouverturePartenaireDetail;
  let fixture: ComponentFixture<CouverturePartenaireDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./couverture-partenaire-detail').then(m => m.CouverturePartenaireDetail),
              resolve: { couverturePartenaire: () => of({ id: 'ad78c8f4-7567-47d0-9aac-cdd545c947f8' }) },
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
    fixture = TestBed.createComponent(CouverturePartenaireDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load couverturePartenaire on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CouverturePartenaireDetail);

      // THEN
      expect(instance.couverturePartenaire()).toEqual(expect.objectContaining({ id: 'ad78c8f4-7567-47d0-9aac-cdd545c947f8' }));
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
