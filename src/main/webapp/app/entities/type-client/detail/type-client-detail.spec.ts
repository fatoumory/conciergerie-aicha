import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TypeClientDetail } from './type-client-detail';

describe('TypeClient Management Detail Component', () => {
  let comp: TypeClientDetail;
  let fixture: ComponentFixture<TypeClientDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./type-client-detail').then(m => m.TypeClientDetail),
              resolve: { typeClient: () => of({ id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' }) },
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
    fixture = TestBed.createComponent(TypeClientDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load typeClient on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeClientDetail);

      // THEN
      expect(instance.typeClient()).toEqual(expect.objectContaining({ id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' }));
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
