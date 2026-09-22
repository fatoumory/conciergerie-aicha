import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ServiceConciergerieDetail } from './service-conciergerie-detail';

describe('ServiceConciergerie Management Detail Component', () => {
  let comp: ServiceConciergerieDetail;
  let fixture: ComponentFixture<ServiceConciergerieDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./service-conciergerie-detail').then(m => m.ServiceConciergerieDetail),
              resolve: { serviceConciergerie: () => of({ id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' }) },
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
    fixture = TestBed.createComponent(ServiceConciergerieDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load serviceConciergerie on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ServiceConciergerieDetail);

      // THEN
      expect(instance.serviceConciergerie()).toEqual(expect.objectContaining({ id: 'f54d1854-fde0-462c-bb5b-1cd05107179d' }));
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
