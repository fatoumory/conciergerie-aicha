import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { UtilisationCodeQrService } from '../service/utilisation-code-qr.service';
import { IUtilisationCodeQr } from '../utilisation-code-qr.model';

@Component({
  templateUrl: './utilisation-code-qr-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class UtilisationCodeQrDeleteDialog {
  utilisationCodeQr?: IUtilisationCodeQr;

  protected readonly utilisationCodeQrService = inject(UtilisationCodeQrService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.utilisationCodeQrService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
