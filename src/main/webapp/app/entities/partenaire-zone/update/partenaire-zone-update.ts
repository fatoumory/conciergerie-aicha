import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { ZoneService } from 'app/entities/zone/service/zone.service';
import { IZone } from 'app/entities/zone/zone.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartenaireZone } from '../partenaire-zone.model';
import { PartenaireZoneService } from '../service/partenaire-zone.service';

import { PartenaireZoneFormGroup, PartenaireZoneFormService } from './partenaire-zone-form.service';

@Component({
  selector: 'jhi-partenaire-zone-update',
  templateUrl: './partenaire-zone-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartenaireZoneUpdate implements OnInit {
  readonly isSaving = signal(false);
  partenaireZone: IPartenaireZone | null = null;

  partenairesSharedCollection = signal<IPartenaire[]>([]);
  zonesSharedCollection = signal<IZone[]>([]);

  protected partenaireZoneService = inject(PartenaireZoneService);
  protected partenaireZoneFormService = inject(PartenaireZoneFormService);
  protected partenaireService = inject(PartenaireService);
  protected zoneService = inject(ZoneService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartenaireZoneFormGroup = this.partenaireZoneFormService.createPartenaireZoneFormGroup();

  comparePartenaire = (o1: IPartenaire | null, o2: IPartenaire | null): boolean => this.partenaireService.comparePartenaire(o1, o2);

  compareZone = (o1: IZone | null, o2: IZone | null): boolean => this.zoneService.compareZone(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partenaireZone }) => {
      this.partenaireZone = partenaireZone;
      if (partenaireZone) {
        this.updateForm(partenaireZone);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partenaireZone = this.partenaireZoneFormService.getPartenaireZone(this.editForm);
    if (partenaireZone.id === null) {
      this.subscribeToSaveResponse(this.partenaireZoneService.create(partenaireZone));
    } else {
      this.subscribeToSaveResponse(this.partenaireZoneService.update(partenaireZone));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartenaireZone | null>): void {
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

  protected updateForm(partenaireZone: IPartenaireZone): void {
    this.partenaireZone = partenaireZone;
    this.partenaireZoneFormService.resetForm(this.editForm, partenaireZone);

    this.partenairesSharedCollection.update(partenaires =>
      this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, partenaireZone.partenaire),
    );
    this.zonesSharedCollection.update(zones => this.zoneService.addZoneToCollectionIfMissing<IZone>(zones, partenaireZone.zone));
  }

  protected loadRelationshipsOptions(): void {
    this.partenaireService
      .query()
      .pipe(map((res: HttpResponse<IPartenaire[]>) => res.body ?? []))
      .pipe(
        map((partenaires: IPartenaire[]) =>
          this.partenaireService.addPartenaireToCollectionIfMissing<IPartenaire>(partenaires, this.partenaireZone?.partenaire),
        ),
      )
      .subscribe((partenaires: IPartenaire[]) => this.partenairesSharedCollection.set(partenaires));

    this.zoneService
      .query()
      .pipe(map((res: HttpResponse<IZone[]>) => res.body ?? []))
      .pipe(map((zones: IZone[]) => this.zoneService.addZoneToCollectionIfMissing<IZone>(zones, this.partenaireZone?.zone)))
      .subscribe((zones: IZone[]) => this.zonesSharedCollection.set(zones));
  }
}
