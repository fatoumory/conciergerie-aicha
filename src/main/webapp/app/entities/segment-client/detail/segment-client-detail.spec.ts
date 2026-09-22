import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { SegmentClientDetail } from './segment-client-detail';

describe('SegmentClient Management Detail Component', () => {
  let comp: SegmentClientDetail;
  let fixture: ComponentFixture<SegmentClientDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./segment-client-detail').then(m => m.SegmentClientDetail),
              resolve: { segmentClient: () => of({ id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' }) },
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
    fixture = TestBed.createComponent(SegmentClientDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load segmentClient on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SegmentClientDetail);

      // THEN
      expect(instance.segmentClient()).toEqual(expect.objectContaining({ id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' }));
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
