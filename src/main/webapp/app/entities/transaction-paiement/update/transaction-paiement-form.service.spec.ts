import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transaction-paiement.test-samples';

import { TransactionPaiementFormService } from './transaction-paiement-form.service';

describe('TransactionPaiement Form Service', () => {
  let service: TransactionPaiementFormService;

  beforeEach(() => {
    service = TestBed.inject(TransactionPaiementFormService);
  });

  describe('Service methods', () => {
    describe('createTransactionPaiementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransactionPaiementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montant: expect.any(Object),
            modePaiement: expect.any(Object),
            statut: expect.any(Object),
            referenceExterne: expect.any(Object),
            dateTransaction: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });

      it('passing ITransactionPaiement should create a new form with FormGroup', () => {
        const formGroup = service.createTransactionPaiementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montant: expect.any(Object),
            modePaiement: expect.any(Object),
            statut: expect.any(Object),
            referenceExterne: expect.any(Object),
            dateTransaction: expect.any(Object),
            demande: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransactionPaiement', () => {
      it('should return NewTransactionPaiement for default TransactionPaiement initial value', () => {
        const formGroup = service.createTransactionPaiementFormGroup(sampleWithNewData);

        const transactionPaiement = service.getTransactionPaiement(formGroup);

        expect(transactionPaiement).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransactionPaiement for empty TransactionPaiement initial value', () => {
        const formGroup = service.createTransactionPaiementFormGroup();

        const transactionPaiement = service.getTransactionPaiement(formGroup);

        expect(transactionPaiement).toMatchObject({});
      });

      it('should return ITransactionPaiement', () => {
        const formGroup = service.createTransactionPaiementFormGroup(sampleWithRequiredData);

        const transactionPaiement = service.getTransactionPaiement(formGroup);

        expect(transactionPaiement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransactionPaiement should not enable id FormControl', () => {
        const formGroup = service.createTransactionPaiementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransactionPaiement should disable id FormControl', () => {
        const formGroup = service.createTransactionPaiementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
