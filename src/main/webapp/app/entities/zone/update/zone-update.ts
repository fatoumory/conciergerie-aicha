import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ZoneService } from '../service/zone.service';
import { IZone } from '../zone.model';

import { ZoneFormGroup, ZoneFormService } from './zone-form.service';

@Component({
  selector: 'jhi-zone-update',
  templateUrl: './zone-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ZoneUpdate implements OnInit {
  readonly isSaving = signal(false);
  zone: IZone | null = null;

  protected zoneService = inject(ZoneService);
  protected zoneFormService = inject(ZoneFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ZoneFormGroup = this.zoneFormService.createZoneFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zone }) => {
      this.zone = zone;
      if (zone) {
        this.updateForm(zone);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const zone = this.zoneFormService.getZone(this.editForm);
    if (zone.id === null) {
      this.subscribeToSaveResponse(this.zoneService.create(zone));
    } else {
      this.subscribeToSaveResponse(this.zoneService.update(zone));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IZone | null>): void {
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

  protected updateForm(zone: IZone): void {
    this.zone = zone;
    this.zoneFormService.resetForm(this.editForm, zone);
  }
}
