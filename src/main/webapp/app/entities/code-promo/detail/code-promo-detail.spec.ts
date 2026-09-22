import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CodePromoDetail } from './code-promo-detail';

describe('CodePromo Management Detail Component', () => {
  let comp: CodePromoDetail;
  let fixture: ComponentFixture<CodePromoDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./code-promo-detail').then(m => m.CodePromoDetail),
              resolve: { codePromo: () => of({ id: 'b727eb78-e176-4142-8685-21300126e80d' }) },
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
    fixture = TestBed.createComponent(CodePromoDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load codePromo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CodePromoDetail);

      // THEN
      expect(instance.codePromo()).toEqual(expect.objectContaining({ id: 'b727eb78-e176-4142-8685-21300126e80d' }));
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
