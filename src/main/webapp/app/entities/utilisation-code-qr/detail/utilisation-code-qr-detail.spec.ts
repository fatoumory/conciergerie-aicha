import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { UtilisationCodeQrDetail } from './utilisation-code-qr-detail';

describe('UtilisationCodeQr Management Detail Component', () => {
  let comp: UtilisationCodeQrDetail;
  let fixture: ComponentFixture<UtilisationCodeQrDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./utilisation-code-qr-detail').then(m => m.UtilisationCodeQrDetail),
              resolve: { utilisationCodeQr: () => of({ id: '9609ad2a-cecd-47c9-a80a-730574aa5903' }) },
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
    fixture = TestBed.createComponent(UtilisationCodeQrDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load utilisationCodeQr on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UtilisationCodeQrDetail);

      // THEN
      expect(instance.utilisationCodeQr()).toEqual(expect.objectContaining({ id: '9609ad2a-cecd-47c9-a80a-730574aa5903' }));
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
