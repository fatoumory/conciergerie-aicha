import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IJournalAudit } from '../journal-audit.model';
import { JournalAuditService } from '../service/journal-audit.service';

import { JournalAuditFormGroup, JournalAuditFormService } from './journal-audit-form.service';

@Component({
  selector: 'jhi-journal-audit-update',
  templateUrl: './journal-audit-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class JournalAuditUpdate implements OnInit {
  readonly isSaving = signal(false);
  journalAudit: IJournalAudit | null = null;

  protected journalAuditService = inject(JournalAuditService);
  protected journalAuditFormService = inject(JournalAuditFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: JournalAuditFormGroup = this.journalAuditFormService.createJournalAuditFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ journalAudit }) => {
      this.journalAudit = journalAudit;
      if (journalAudit) {
        this.updateForm(journalAudit);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const journalAudit = this.journalAuditFormService.getJournalAudit(this.editForm);
    if (journalAudit.id === null) {
      this.subscribeToSaveResponse(this.journalAuditService.create(journalAudit));
    } else {
      this.subscribeToSaveResponse(this.journalAuditService.update(journalAudit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IJournalAudit | null>): void {
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

  protected updateForm(journalAudit: IJournalAudit): void {
    this.journalAudit = journalAudit;
    this.journalAuditFormService.resetForm(this.editForm, journalAudit);
  }
}
