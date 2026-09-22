import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { ICodePromo } from 'app/entities/code-promo/code-promo.model';
import { CodePromoService } from 'app/entities/code-promo/service/code-promo.service';
import { ServiceConciergerieService } from 'app/entities/service-conciergerie/service/service-conciergerie.service';
import { IServiceConciergerie } from 'app/entities/service-conciergerie/service-conciergerie.model';
import { IStatutDemande } from 'app/entities/statut-demande/statut-demande.model';
import { TypeDemandeService } from 'app/entities/type-demande/service/type-demande.service';
import { ITypeDemande } from 'app/entities/type-demande/type-demande.model';
import { StatutDemandeService } from 'app/entities/statut-demande/service/statut-demande.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IDemande } from '../demande.model';
import { DemandeService } from '../service/demande.service';

import { DemandeFormGroup, DemandeFormService } from './demande-form.service';

@Component({
  selector: 'jhi-demande-update',
  templateUrl: './demande-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DemandeUpdate implements OnInit {
  readonly isSaving = signal(false);
  demande: IDemande | null = null;

  clientsSharedCollection = signal<IClient[]>([]);
  serviceConciergeriesSharedCollection = signal<IServiceConciergerie[]>([]);
  typeDemandesSharedCollection = signal<ITypeDemande[]>([]);
  statutDemandesSharedCollection = signal<IStatutDemande[]>([]);
  codePromosSharedCollection = signal<ICodePromo[]>([]);

  protected demandeService = inject(DemandeService);
  protected demandeFormService = inject(DemandeFormService);
  protected clientService = inject(ClientService);
  protected serviceConciergerieService = inject(ServiceConciergerieService);
  protected typeDemandeService = inject(TypeDemandeService);
  protected statutDemandeService = inject(StatutDemandeService);
  protected codePromoService = inject(CodePromoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DemandeFormGroup = this.demandeFormService.createDemandeFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareServiceConciergerie = (o1: IServiceConciergerie | null, o2: IServiceConciergerie | null): boolean =>
    this.serviceConciergerieService.compareServiceConciergerie(o1, o2);

  compareTypeDemande = (o1: ITypeDemande | null, o2: ITypeDemande | null): boolean => this.typeDemandeService.compareTypeDemande(o1, o2);

  compareStatutDemande = (o1: IStatutDemande | null, o2: IStatutDemande | null): boolean =>
    this.statutDemandeService.compareStatutDemande(o1, o2);

  compareCodePromo = (o1: ICodePromo | null, o2: ICodePromo | null): boolean => this.codePromoService.compareCodePromo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demande }) => {
      this.demande = demande;
      if (demande) {
        this.updateForm(demande);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const demande = this.demandeFormService.getDemande(this.editForm);
    if (demande.id === null) {
      this.subscribeToSaveResponse(this.demandeService.create(demande));
    } else {
      this.subscribeToSaveResponse(this.demandeService.update(demande));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDemande | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(demande: IDemande): void {
    this.demande = demande;
    this.demandeFormService.resetForm(this.editForm, demande);

    this.clientsSharedCollection.update(clients => this.clientService.addClientToCollectionIfMissing<IClient>(clients, demande.client));
    this.serviceConciergeriesSharedCollection.update(serviceConciergeries =>
      this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
        serviceConciergeries,
        demande.service,
      ),
    );
    this.typeDemandesSharedCollection.update(typeDemandes =>
      this.typeDemandeService.addTypeDemandeToCollectionIfMissing<ITypeDemande>(typeDemandes, demande.typeDemande),
    );
    this.statutDemandesSharedCollection.update(statutDemandes =>
      this.statutDemandeService.addStatutDemandeToCollectionIfMissing<IStatutDemande>(statutDemandes, demande.statut),
    );
    this.codePromosSharedCollection.update(codePromos =>
      this.codePromoService.addCodePromoToCollectionIfMissing<ICodePromo>(codePromos, demande.codePromo),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.demande?.client)))
      .subscribe((clients: IClient[]) => this.clientsSharedCollection.set(clients));

    this.serviceConciergerieService
      .query()
      .pipe(map((res: HttpResponse<IServiceConciergerie[]>) => res.body ?? []))
      .pipe(
        map((serviceConciergeries: IServiceConciergerie[]) =>
          this.serviceConciergerieService.addServiceConciergerieToCollectionIfMissing<IServiceConciergerie>(
            serviceConciergeries,
            this.demande?.service,
          ),
        ),
      )
      .subscribe((serviceConciergeries: IServiceConciergerie[]) => this.serviceConciergeriesSharedCollection.set(serviceConciergeries));

    this.typeDemandeService
      .query()
      .pipe(map((res: HttpResponse<ITypeDemande[]>) => res.body ?? []))
      .pipe(
        map((typeDemandes: ITypeDemande[]) =>
          this.typeDemandeService.addTypeDemandeToCollectionIfMissing<ITypeDemande>(typeDemandes, this.demande?.typeDemande),
        ),
      )
      .subscribe((typeDemandes: ITypeDemande[]) => this.typeDemandesSharedCollection.set(typeDemandes));

    this.statutDemandeService
      .query()
      .pipe(map((res: HttpResponse<IStatutDemande[]>) => res.body ?? []))
      .pipe(
        map((statutDemandes: IStatutDemande[]) =>
          this.statutDemandeService.addStatutDemandeToCollectionIfMissing<IStatutDemande>(statutDemandes, this.demande?.statut),
        ),
      )
      .subscribe((statutDemandes: IStatutDemande[]) => this.statutDemandesSharedCollection.set(statutDemandes));

    this.codePromoService
      .query()
      .pipe(map((res: HttpResponse<ICodePromo[]>) => res.body ?? []))
      .pipe(
        map((codePromos: ICodePromo[]) =>
          this.codePromoService.addCodePromoToCollectionIfMissing<ICodePromo>(codePromos, this.demande?.codePromo),
        ),
      )
      .subscribe((codePromos: ICodePromo[]) => this.codePromosSharedCollection.set(codePromos));
  }
}
