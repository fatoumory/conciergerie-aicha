import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { of } from 'rxjs';

import { CodeQrServiceService } from '../service/code-qr-service.service';

import { CodeQrServiceDeleteDialog } from './code-qr-service-delete-dialog';

describe('CodeQrService Management Delete Component', () => {
  let comp: CodeQrServiceDeleteDialog;
  let fixture: ComponentFixture<CodeQrServiceDeleteDialog>;
  let service: CodeQrServiceService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NgbActiveModal],
    });
    fixture = TestBed.createComponent(CodeQrServiceDeleteDialog);
    comp = fixture.componentInstance;
    service = TestBed.inject(CodeQrServiceService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', () => {
      // GIVEN
      vi.spyOn(service, 'delete').mockReturnValue(of(undefined));
      vi.spyOn(mockActiveModal, 'close');

      // WHEN
      comp.confirmDelete('9fec3727-3421-4967-b213-ba36557ca194');

      // THEN
      expect(service.delete).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
      expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
    });
  });

  describe('cancel', () => {
    it('should not call delete service on cancel', () => {
      // GIVEN
      vi.spyOn(service, 'delete');
      vi.spyOn(mockActiveModal, 'close');
      vi.spyOn(mockActiveModal, 'dismiss');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
