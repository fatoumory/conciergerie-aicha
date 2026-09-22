import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { TransactionPaiementService } from '../service/transaction-paiement.service';
import { ITransactionPaiement } from '../transaction-paiement.model';

import { TransactionPaiementFormService } from './transaction-paiement-form.service';
import { TransactionPaiementUpdate } from './transaction-paiement-update';

describe('TransactionPaiement Management Update Component', () => {
  let comp: TransactionPaiementUpdate;
  let fixture: ComponentFixture<TransactionPaiementUpdate>;
  let activatedRoute: ActivatedRoute;
  let transactionPaiementFormService: TransactionPaiementFormService;
  let transactionPaiementService: TransactionPaiementService;
  let demandeService: DemandeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(TransactionPaiementUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionPaiementFormService = TestBed.inject(TransactionPaiementFormService);
    transactionPaiementService = TestBed.inject(TransactionPaiementService);
    demandeService = TestBed.inject(DemandeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Demande query and add missing value', () => {
      const transactionPaiement: ITransactionPaiement = { id: '0057cb97-0599-4416-8446-1654644a3cfd' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      transactionPaiement.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' }];
      vi.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const additionalDemandes = [demande];
      const expectedCollection: IDemande[] = [...additionalDemandes, ...demandeCollection];
      vi.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionPaiement });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(
        demandeCollection,
        ...additionalDemandes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.demandesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const transactionPaiement: ITransactionPaiement = { id: '0057cb97-0599-4416-8446-1654644a3cfd' };
      const demande: IDemande = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
      transactionPaiement.demande = demande;

      activatedRoute.data = of({ transactionPaiement });
      comp.ngOnInit();

      expect(comp.demandesSharedCollection()).toContainEqual(demande);
      expect(comp.transactionPaiement).toEqual(transactionPaiement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionPaiement>();
      const transactionPaiement = { id: '33f8355d-c755-4a93-9672-44338f6f0121' };
      vi.spyOn(transactionPaiementFormService, 'getTransactionPaiement').mockReturnValue(transactionPaiement);
      vi.spyOn(transactionPaiementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionPaiement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionPaiement);
      saveSubject.complete();

      // THEN
      expect(transactionPaiementFormService.getTransactionPaiement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionPaiementService.update).toHaveBeenCalledWith(expect.objectContaining(transactionPaiement));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionPaiement>();
      const transactionPaiement = { id: '33f8355d-c755-4a93-9672-44338f6f0121' };
      vi.spyOn(transactionPaiementFormService, 'getTransactionPaiement').mockReturnValue({ id: null });
      vi.spyOn(transactionPaiementService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionPaiement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionPaiement);
      saveSubject.complete();

      // THEN
      expect(transactionPaiementFormService.getTransactionPaiement).toHaveBeenCalled();
      expect(transactionPaiementService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionPaiement>();
      const transactionPaiement = { id: '33f8355d-c755-4a93-9672-44338f6f0121' };
      vi.spyOn(transactionPaiementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionPaiement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionPaiementService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDemande', () => {
      it('should forward to demandeService', () => {
        const entity = { id: 'db61c811-f5b6-4f5a-a362-3462da216ecd' };
        const entity2 = { id: 'b6254aa1-6b39-4790-b854-bb8a65052db5' };
        vi.spyOn(demandeService, 'compareDemande');
        comp.compareDemande(entity, entity2);
        expect(demandeService.compareDemande).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
