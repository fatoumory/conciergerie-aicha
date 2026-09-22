import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PartenaireDetail } from './partenaire-detail';

describe('Partenaire Management Detail Component', () => {
  let comp: PartenaireDetail;
  let fixture: ComponentFixture<PartenaireDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./partenaire-detail').then(m => m.PartenaireDetail),
              resolve: { partenaire: () => of({ id: 'd330918e-c880-4b4f-a4ba-30a19287c322' }) },
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
    fixture = TestBed.createComponent(PartenaireDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load partenaire on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PartenaireDetail);

      // THEN
      expect(instance.partenaire()).toEqual(expect.objectContaining({ id: 'd330918e-c880-4b4f-a4ba-30a19287c322' }));
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
