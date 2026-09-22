import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PartenaireZoneDetail } from './partenaire-zone-detail';

describe('PartenaireZone Management Detail Component', () => {
  let comp: PartenaireZoneDetail;
  let fixture: ComponentFixture<PartenaireZoneDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./partenaire-zone-detail').then(m => m.PartenaireZoneDetail),
              resolve: { partenaireZone: () => of({ id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' }) },
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
    fixture = TestBed.createComponent(PartenaireZoneDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load partenaireZone on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PartenaireZoneDetail);

      // THEN
      expect(instance.partenaireZone()).toEqual(expect.objectContaining({ id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' }));
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
