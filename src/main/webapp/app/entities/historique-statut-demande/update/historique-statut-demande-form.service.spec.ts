import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../historique-statut-demande.test-samples';

import { HistoriqueStatutDemandeFormService } from './historique-statut-demande-form.service';

describe('HistoriqueStatutDemande Form Service', () => {
  let service: HistoriqueStatutDemandeFormService;

  beforeEach(() => {
    service = TestBed.inject(HistoriqueStatutDemandeFormService);
  });

  describe('Service methods', () => {
    describe('createHistoriqueStatutDemandeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHistoriqueStatutDemandeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateChangement: expect.any(Object),
            demande: expect.any(Object),
            statut: expect.any(Object),
          }),
        );
      });

      it('passing IHistoriqueStatutDemande should create a new form with FormGroup', () => {
        const formGroup = service.createHistoriqueStatutDemandeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateChangement: expect.any(Object),
            demande: expect.any(Object),
            statut: expect.any(Object),
          }),
        );
      });
    });

    describe('getHistoriqueStatutDemande', () => {
      it('should return NewHistoriqueStatutDemande for default HistoriqueStatutDemande initial value', () => {
        const formGroup = service.createHistoriqueStatutDemandeFormGroup(sampleWithNewData);

        const historiqueStatutDemande = service.getHistoriqueStatutDemande(formGroup);

        expect(historiqueStatutDemande).toMatchObject(sampleWithNewData);
      });

      it('should return NewHistoriqueStatutDemande for empty HistoriqueStatutDemande initial value', () => {
        const formGroup = service.createHistoriqueStatutDemandeFormGroup();

        const historiqueStatutDemande = service.getHistoriqueStatutDemande(formGroup);

        expect(historiqueStatutDemande).toMatchObject({});
      });

      it('should return IHistoriqueStatutDemande', () => {
        const formGroup = service.createHistoriqueStatutDemandeFormGroup(sampleWithRequiredData);

        const historiqueStatutDemande = service.getHistoriqueStatutDemande(formGroup);

        expect(historiqueStatutDemande).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHistoriqueStatutDemande should not enable id FormControl', () => {
        const formGroup = service.createHistoriqueStatutDemandeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHistoriqueStatutDemande should disable id FormControl', () => {
        const formGroup = service.createHistoriqueStatutDemandeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
