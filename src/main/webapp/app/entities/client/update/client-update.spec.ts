import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ISegmentClient } from 'app/entities/segment-client/segment-client.model';
import { SegmentClientService } from 'app/entities/segment-client/service/segment-client.service';
import { TypeClientService } from 'app/entities/type-client/service/type-client.service';
import { ITypeClient } from 'app/entities/type-client/type-client.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';

import { ClientFormService } from './client-form.service';
import { ClientUpdate } from './client-update';

describe('Client Management Update Component', () => {
  let comp: ClientUpdate;
  let fixture: ComponentFixture<ClientUpdate>;
  let activatedRoute: ActivatedRoute;
  let clientFormService: ClientFormService;
  let clientService: ClientService;
  let userService: UserService;
  let typeClientService: TypeClientService;
  let segmentClientService: SegmentClientService;

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

    fixture = TestBed.createComponent(ClientUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clientFormService = TestBed.inject(ClientFormService);
    clientService = TestBed.inject(ClientService);
    userService = TestBed.inject(UserService);
    typeClientService = TestBed.inject(TypeClientService);
    segmentClientService = TestBed.inject(SegmentClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const client: IClient = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
      const user: IUser = { id: 3944 };
      client.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ client });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TypeClient query and add missing value', () => {
      const client: IClient = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
      const typeClient: ITypeClient = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
      client.typeClient = typeClient;

      const typeClientCollection: ITypeClient[] = [{ id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' }];
      vi.spyOn(typeClientService, 'query').mockReturnValue(of(new HttpResponse({ body: typeClientCollection })));
      const additionalTypeClients = [typeClient];
      const expectedCollection: ITypeClient[] = [...additionalTypeClients, ...typeClientCollection];
      vi.spyOn(typeClientService, 'addTypeClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ client });
      comp.ngOnInit();

      expect(typeClientService.query).toHaveBeenCalled();
      expect(typeClientService.addTypeClientToCollectionIfMissing).toHaveBeenCalledWith(
        typeClientCollection,
        ...additionalTypeClients.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.typeClientsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call SegmentClient query and add missing value', () => {
      const client: IClient = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
      const segmentClient: ISegmentClient = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
      client.segmentClient = segmentClient;

      const segmentClientCollection: ISegmentClient[] = [{ id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' }];
      vi.spyOn(segmentClientService, 'query').mockReturnValue(of(new HttpResponse({ body: segmentClientCollection })));
      const additionalSegmentClients = [segmentClient];
      const expectedCollection: ISegmentClient[] = [...additionalSegmentClients, ...segmentClientCollection];
      vi.spyOn(segmentClientService, 'addSegmentClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ client });
      comp.ngOnInit();

      expect(segmentClientService.query).toHaveBeenCalled();
      expect(segmentClientService.addSegmentClientToCollectionIfMissing).toHaveBeenCalledWith(
        segmentClientCollection,
        ...additionalSegmentClients.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.segmentClientsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const client: IClient = { id: '687bb8c0-68bf-47c8-b217-55897bbb16b1' };
      const user: IUser = { id: 3944 };
      client.user = user;
      const typeClient: ITypeClient = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
      client.typeClient = typeClient;
      const segmentClient: ISegmentClient = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
      client.segmentClient = segmentClient;

      activatedRoute.data = of({ client });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(user);
      expect(comp.typeClientsSharedCollection()).toContainEqual(typeClient);
      expect(comp.segmentClientsSharedCollection()).toContainEqual(segmentClient);
      expect(comp.client).toEqual(client);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IClient>();
      const client = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      vi.spyOn(clientFormService, 'getClient').mockReturnValue(client);
      vi.spyOn(clientService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ client });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(client);
      saveSubject.complete();

      // THEN
      expect(clientFormService.getClient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clientService.update).toHaveBeenCalledWith(expect.objectContaining(client));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IClient>();
      const client = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      vi.spyOn(clientFormService, 'getClient').mockReturnValue({ id: null });
      vi.spyOn(clientService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ client: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(client);
      saveSubject.complete();

      // THEN
      expect(clientFormService.getClient).toHaveBeenCalled();
      expect(clientService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IClient>();
      const client = { id: 'c8e41822-3cf0-4cba-a9bb-eac01142b7ff' };
      vi.spyOn(clientService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ client });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clientService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vi.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeClient', () => {
      it('should forward to typeClientService', () => {
        const entity = { id: 'd3edaf1a-b61b-49ca-beb4-167f25744ead' };
        const entity2 = { id: 'c3bcad5f-3b94-4cfe-b1a6-4c3b86b45e40' };
        vi.spyOn(typeClientService, 'compareTypeClient');
        comp.compareTypeClient(entity, entity2);
        expect(typeClientService.compareTypeClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSegmentClient', () => {
      it('should forward to segmentClientService', () => {
        const entity = { id: '6ca2c935-8cf0-4d27-b38f-08fa9e7b9333' };
        const entity2 = { id: '3dfe0be4-098d-4b25-9305-36dbaeacd2d1' };
        vi.spyOn(segmentClientService, 'compareSegmentClient');
        comp.compareSegmentClient(entity, entity2);
        expect(segmentClientService.compareSegmentClient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
