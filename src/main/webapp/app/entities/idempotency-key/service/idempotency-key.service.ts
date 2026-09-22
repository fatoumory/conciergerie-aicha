import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IIdempotencyKey, NewIdempotencyKey } from '../idempotency-key.model';

export type PartialUpdateIdempotencyKey = Partial<IIdempotencyKey> & Pick<IIdempotencyKey, 'id'>;

type RestOf<T extends IIdempotencyKey | NewIdempotencyKey> = Omit<T, 'dateCreation' | 'dateExpiration'> & {
  dateCreation?: string | null;
  dateExpiration?: string | null;
};

export type RestIdempotencyKey = RestOf<IIdempotencyKey>;

export type NewRestIdempotencyKey = RestOf<NewIdempotencyKey>;

export type PartialUpdateRestIdempotencyKey = RestOf<PartialUpdateIdempotencyKey>;

@Service()
export class IdempotencyKeysService {
  readonly idempotencyKeysParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly idempotencyKeysResource = httpResource<RestIdempotencyKey[]>(() => {
    const params = this.idempotencyKeysParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of idempotencyKey that have been fetched. It is updated when the idempotencyKeysResource emits a new value.
   * In case of error while fetching the idempotencyKeys, the signal is set to an empty array.
   */
  readonly idempotencyKeys = computed(() =>
    (this.idempotencyKeysResource.hasValue() ? this.idempotencyKeysResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/idempotency-keys`;

  protected convertValueFromServer(restIdempotencyKey: RestIdempotencyKey): IIdempotencyKey {
    return {
      ...restIdempotencyKey,
      dateCreation: restIdempotencyKey.dateCreation ? dayjs(restIdempotencyKey.dateCreation) : undefined,
      dateExpiration: restIdempotencyKey.dateExpiration ? dayjs(restIdempotencyKey.dateExpiration) : undefined,
    };
  }
}

@Service()
export class IdempotencyKeyService extends IdempotencyKeysService {
  protected readonly http = inject(HttpClient);

  create(idempotencyKey: NewIdempotencyKey): Observable<IIdempotencyKey> {
    const copy = this.convertValueFromClient(idempotencyKey);
    return this.http.post<RestIdempotencyKey>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(idempotencyKey: IIdempotencyKey): Observable<IIdempotencyKey> {
    const copy = this.convertValueFromClient(idempotencyKey);
    return this.http
      .put<RestIdempotencyKey>(`${this.resourceUrl}/${encodeURIComponent(this.getIdempotencyKeyIdentifier(idempotencyKey))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(idempotencyKey: PartialUpdateIdempotencyKey): Observable<IIdempotencyKey> {
    const copy = this.convertValueFromClient(idempotencyKey);
    return this.http
      .patch<RestIdempotencyKey>(`${this.resourceUrl}/${encodeURIComponent(this.getIdempotencyKeyIdentifier(idempotencyKey))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IIdempotencyKey> {
    return this.http
      .get<RestIdempotencyKey>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IIdempotencyKey[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIdempotencyKey[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getIdempotencyKeyIdentifier(idempotencyKey: Pick<IIdempotencyKey, 'id'>): string {
    return idempotencyKey.id;
  }

  compareIdempotencyKey(o1: Pick<IIdempotencyKey, 'id'> | null, o2: Pick<IIdempotencyKey, 'id'> | null): boolean {
    return o1 && o2 ? this.getIdempotencyKeyIdentifier(o1) === this.getIdempotencyKeyIdentifier(o2) : o1 === o2;
  }

  addIdempotencyKeyToCollectionIfMissing<Type extends Pick<IIdempotencyKey, 'id'>>(
    idempotencyKeyCollection: Type[],
    ...idempotencyKeysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const idempotencyKeys: Type[] = idempotencyKeysToCheck.filter(
      idempotencyKeyItem => idempotencyKeyItem !== null && idempotencyKeyItem !== undefined,
    );
    if (idempotencyKeys.length > 0) {
      const idempotencyKeyCollectionIdentifiers = idempotencyKeyCollection.map(idempotencyKeyItem =>
        this.getIdempotencyKeyIdentifier(idempotencyKeyItem),
      );
      const idempotencyKeysToAdd = idempotencyKeys.filter(idempotencyKeyItem => {
        const idempotencyKeyIdentifier = this.getIdempotencyKeyIdentifier(idempotencyKeyItem);
        if (idempotencyKeyCollectionIdentifiers.includes(idempotencyKeyIdentifier)) {
          return false;
        }
        idempotencyKeyCollectionIdentifiers.push(idempotencyKeyIdentifier);
        return true;
      });
      return [...idempotencyKeysToAdd, ...idempotencyKeyCollection];
    }
    return idempotencyKeyCollection;
  }

  protected convertValueFromClient<T extends IIdempotencyKey | NewIdempotencyKey | PartialUpdateIdempotencyKey>(
    idempotencyKey: T,
  ): RestOf<T> {
    return {
      ...idempotencyKey,
      dateCreation: idempotencyKey.dateCreation?.toJSON() ?? null,
      dateExpiration: idempotencyKey.dateExpiration?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestIdempotencyKey): IIdempotencyKey {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestIdempotencyKey[]): IIdempotencyKey[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
