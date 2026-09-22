import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TypeServiceDetail } from './type-service-detail';

describe('TypeService Management Detail Component', () => {
  let comp: TypeServiceDetail;
  let fixture: ComponentFixture<TypeServiceDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./type-service-detail').then(m => m.TypeServiceDetail),
              resolve: { typeService: () => of({ id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' }) },
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
    fixture = TestBed.createComponent(TypeServiceDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load typeService on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeServiceDetail);

      // THEN
      expect(instance.typeService()).toEqual(expect.objectContaining({ id: '8cb46a60-6af2-43f2-960f-18bb3ebe9763' }));
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
