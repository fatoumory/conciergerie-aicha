import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IAuthority } from 'app/entities/admin/authority/authority.model';
import { AuthorityService } from 'app/entities/admin/authority/service/authority.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';

import { ProfilFormGroup, ProfilFormService } from './profil-form.service';

@Component({
  selector: 'jhi-profil-update',
  templateUrl: './profil-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProfilUpdate implements OnInit {
  readonly isSaving = signal(false);
  profil: IProfil | null = null;

  usersSharedCollection = signal<IUser[]>([]);
  authoritiesSharedCollection = signal<IAuthority[]>([]);

  protected profilService = inject(ProfilService);
  protected profilFormService = inject(ProfilFormService);
  protected userService = inject(UserService);
  protected authorityService = inject(AuthorityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfilFormGroup = this.profilFormService.createProfilFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareAuthority = (o1: IAuthority | null, o2: IAuthority | null): boolean => this.authorityService.compareAuthority(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profil }) => {
      this.profil = profil;
      if (profil) {
        this.updateForm(profil);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const profil = this.profilFormService.getProfil(this.editForm);
    if (profil.id === null) {
      this.subscribeToSaveResponse(this.profilService.create(profil));
    } else {
      this.subscribeToSaveResponse(this.profilService.update(profil));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProfil | null>): void {
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

  protected updateForm(profil: IProfil): void {
    this.profil = profil;
    this.profilFormService.resetForm(this.editForm, profil);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(profil.utilisateurs ?? [])));
    this.authoritiesSharedCollection.update(authorities =>
      this.authorityService.addAuthorityToCollectionIfMissing<IAuthority>(authorities, ...(profil.roles ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(this.profil?.utilisateurs ?? []))))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));

    this.authorityService
      .query()
      .pipe(map((res: HttpResponse<IAuthority[]>) => res.body ?? []))
      .pipe(
        map((authorities: IAuthority[]) =>
          this.authorityService.addAuthorityToCollectionIfMissing<IAuthority>(authorities, ...(this.profil?.roles ?? [])),
        ),
      )
      .subscribe((authorities: IAuthority[]) => this.authoritiesSharedCollection.set(authorities));
  }
}
