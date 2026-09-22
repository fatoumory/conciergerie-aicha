import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { IdempotencyKeyDetail } from './idempotency-key-detail';

describe('IdempotencyKey Management Detail Component', () => {
  let comp: IdempotencyKeyDetail;
  let fixture: ComponentFixture<IdempotencyKeyDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./idempotency-key-detail').then(m => m.IdempotencyKeyDetail),
              resolve: { idempotencyKey: () => of({ id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' }) },
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
    fixture = TestBed.createComponent(IdempotencyKeyDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load idempotencyKey on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', IdempotencyKeyDetail);

      // THEN
      expect(instance.idempotencyKey()).toEqual(expect.objectContaining({ id: '83aa75a4-0019-4218-9bc4-3fcdf60c49b7' }));
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
