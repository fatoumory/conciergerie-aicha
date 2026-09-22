import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAuthority } from 'app/entities/admin/authority/authority.model';
import { AuthorityService } from 'app/entities/admin/authority/service/authority.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';

import { ProfilFormService } from './profil-form.service';
import { ProfilUpdate } from './profil-update';

describe('Profil Management Update Component', () => {
  let comp: ProfilUpdate;
  let fixture: ComponentFixture<ProfilUpdate>;
  let activatedRoute: ActivatedRoute;
  let profilFormService: ProfilFormService;
  let profilService: ProfilService;
  let userService: UserService;
  let authorityService: AuthorityService;

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

    fixture = TestBed.createComponent(ProfilUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    profilFormService = TestBed.inject(ProfilFormService);
    profilService = TestBed.inject(ProfilService);
    userService = TestBed.inject(UserService);
    authorityService = TestBed.inject(AuthorityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const profil: IProfil = { id: '35d1435d-cad0-4267-b809-22ba06c71b4a' };
      const utilisateurs: IUser[] = [{ id: 3944 }];
      profil.utilisateurs = utilisateurs;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [...utilisateurs];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Authority query and add missing value', () => {
      const profil: IProfil = { id: '35d1435d-cad0-4267-b809-22ba06c71b4a' };
      const roles: IAuthority[] = [{ name: '572a7ecc-bf76-43f4-8026-46b42fba586d' }];
      profil.roles = roles;

      const authorityCollection: IAuthority[] = [{ name: '572a7ecc-bf76-43f4-8026-46b42fba586d' }];
      vi.spyOn(authorityService, 'query').mockReturnValue(of(new HttpResponse({ body: authorityCollection })));
      const additionalAuthorities = [...roles];
      const expectedCollection: IAuthority[] = [...additionalAuthorities, ...authorityCollection];
      vi.spyOn(authorityService, 'addAuthorityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(authorityService.query).toHaveBeenCalled();
      expect(authorityService.addAuthorityToCollectionIfMissing).toHaveBeenCalledWith(
        authorityCollection,
        ...additionalAuthorities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.authoritiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const profil: IProfil = { id: '35d1435d-cad0-4267-b809-22ba06c71b4a' };
      const utilisateur: IUser = { id: 3944 };
      profil.utilisateurs = [utilisateur];
      const role: IAuthority = { name: '572a7ecc-bf76-43f4-8026-46b42fba586d' };
      profil.roles = [role];

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(utilisateur);
      expect(comp.authoritiesSharedCollection()).toContainEqual(role);
      expect(comp.profil).toEqual(profil);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProfil>();
      const profil = { id: '56981eb1-c9ea-4f7a-a914-91454668ffa6' };
      vi.spyOn(profilFormService, 'getProfil').mockReturnValue(profil);
      vi.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(profil);
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(profilService.update).toHaveBeenCalledWith(expect.objectContaining(profil));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProfil>();
      const profil = { id: '56981eb1-c9ea-4f7a-a914-91454668ffa6' };
      vi.spyOn(profilFormService, 'getProfil').mockReturnValue({ id: null });
      vi.spyOn(profilService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(profil);
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(profilService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProfil>();
      const profil = { id: '56981eb1-c9ea-4f7a-a914-91454668ffa6' };
      vi.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(profilService.update).toHaveBeenCalled();
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

    describe('compareAuthority', () => {
      it('should forward to authorityService', () => {
        const entity = { name: '572a7ecc-bf76-43f4-8026-46b42fba586d' };
        const entity2 = { name: 'c56c1cf7-aca8-48fe-ad81-eeebbf872cb1' };
        vi.spyOn(authorityService, 'compareAuthority');
        comp.compareAuthority(entity, entity2);
        expect(authorityService.compareAuthority).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
