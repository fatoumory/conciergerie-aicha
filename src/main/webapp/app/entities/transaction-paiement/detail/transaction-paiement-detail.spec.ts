import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TransactionPaiementDetail } from './transaction-paiement-detail';

describe('TransactionPaiement Management Detail Component', () => {
  let comp: TransactionPaiementDetail;
  let fixture: ComponentFixture<TransactionPaiementDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./transaction-paiement-detail').then(m => m.TransactionPaiementDetail),
              resolve: { transactionPaiement: () => of({ id: '33f8355d-c755-4a93-9672-44338f6f0121' }) },
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
    fixture = TestBed.createComponent(TransactionPaiementDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load transactionPaiement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TransactionPaiementDetail);

      // THEN
      expect(instance.transactionPaiement()).toEqual(expect.objectContaining({ id: '33f8355d-c755-4a93-9672-44338f6f0121' }));
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
