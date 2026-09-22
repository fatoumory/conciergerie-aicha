import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { HistoriqueStatutDemandeDetail } from './historique-statut-demande-detail';

describe('HistoriqueStatutDemande Management Detail Component', () => {
  let comp: HistoriqueStatutDemandeDetail;
  let fixture: ComponentFixture<HistoriqueStatutDemandeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./historique-statut-demande-detail').then(m => m.HistoriqueStatutDemandeDetail),
              resolve: { historiqueStatutDemande: () => of({ id: 'b034fefc-0610-4162-94d9-de92093e7063' }) },
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
    fixture = TestBed.createComponent(HistoriqueStatutDemandeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load historiqueStatutDemande on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HistoriqueStatutDemandeDetail);

      // THEN
      expect(instance.historiqueStatutDemande()).toEqual(expect.objectContaining({ id: 'b034fefc-0610-4162-94d9-de92093e7063' }));
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
