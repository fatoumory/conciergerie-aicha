import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ISegmentClient } from 'app/entities/segment-client/segment-client.model';
import { SegmentClientService } from 'app/entities/segment-client/service/segment-client.service';
import { TypeClientService } from 'app/entities/type-client/service/type-client.service';
import { ITypeClient } from 'app/entities/type-client/type-client.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';

import { ClientFormGroup, ClientFormService } from './client-form.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ClientUpdate implements OnInit {
  readonly isSaving = signal(false);
  client: IClient | null = null;

  usersSharedCollection = signal<IUser[]>([]);
  typeClientsSharedCollection = signal<ITypeClient[]>([]);
  segmentClientsSharedCollection = signal<ISegmentClient[]>([]);

  protected clientService = inject(ClientService);
  protected clientFormService = inject(ClientFormService);
  protected userService = inject(UserService);
  protected typeClientService = inject(TypeClientService);
  protected segmentClientService = inject(SegmentClientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClientFormGroup = this.clientFormService.createClientFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareTypeClient = (o1: ITypeClient | null, o2: ITypeClient | null): boolean => this.typeClientService.compareTypeClient(o1, o2);

  compareSegmentClient = (o1: ISegmentClient | null, o2: ISegmentClient | null): boolean =>
    this.segmentClientService.compareSegmentClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.client = client;
      if (client) {
        this.updateForm(client);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const client = this.clientFormService.getClient(this.editForm);
    if (client.id === null) {
      this.subscribeToSaveResponse(this.clientService.create(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.update(client));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IClient | null>): void {
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

  protected updateForm(client: IClient): void {
    this.client = client;
    this.clientFormService.resetForm(this.editForm, client);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, client.user));
    this.typeClientsSharedCollection.update(typeClients =>
      this.typeClientService.addTypeClientToCollectionIfMissing<ITypeClient>(typeClients, client.typeClient),
    );
    this.segmentClientsSharedCollection.update(segmentClients =>
      this.segmentClientService.addSegmentClientToCollectionIfMissing<ISegmentClient>(segmentClients, client.segmentClient),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.client?.user)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));

    this.typeClientService
      .query()
      .pipe(map((res: HttpResponse<ITypeClient[]>) => res.body ?? []))
      .pipe(
        map((typeClients: ITypeClient[]) =>
          this.typeClientService.addTypeClientToCollectionIfMissing<ITypeClient>(typeClients, this.client?.typeClient),
        ),
      )
      .subscribe((typeClients: ITypeClient[]) => this.typeClientsSharedCollection.set(typeClients));

    this.segmentClientService
      .query()
      .pipe(map((res: HttpResponse<ISegmentClient[]>) => res.body ?? []))
      .pipe(
        map((segmentClients: ISegmentClient[]) =>
          this.segmentClientService.addSegmentClientToCollectionIfMissing<ISegmentClient>(segmentClients, this.client?.segmentClient),
        ),
      )
      .subscribe((segmentClients: ISegmentClient[]) => this.segmentClientsSharedCollection.set(segmentClients));
  }
}
