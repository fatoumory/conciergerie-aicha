import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EligibiliteServiceDetail } from './eligibilite-service-detail';

describe('EligibiliteService Management Detail Component', () => {
  let comp: EligibiliteServiceDetail;
  let fixture: ComponentFixture<EligibiliteServiceDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./eligibilite-service-detail').then(m => m.EligibiliteServiceDetail),
              resolve: { eligibiliteService: () => of({ id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' }) },
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
    fixture = TestBed.createComponent(EligibiliteServiceDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load eligibiliteService on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EligibiliteServiceDetail);

      // THEN
      expect(instance.eligibiliteService()).toEqual(expect.objectContaining({ id: 'b71c6aa4-4ba4-4639-b209-31b0ecdb2f92' }));
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
