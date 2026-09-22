import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITypeClient, NewTypeClient } from '../type-client.model';

export type PartialUpdateTypeClient = Partial<ITypeClient> & Pick<ITypeClient, 'id'>;

@Service()
export class TypeClientsService {
  readonly typeClientsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly typeClientsResource = httpResource<ITypeClient[]>(() => {
    const params = this.typeClientsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of typeClient that have been fetched. It is updated when the typeClientsResource emits a new value.
   * In case of error while fetching the typeClients, the signal is set to an empty array.
   */
  readonly typeClients = computed(() => (this.typeClientsResource.hasValue() ? this.typeClientsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/type-clients`;
}

@Service()
export class TypeClientService extends TypeClientsService {
  protected readonly http = inject(HttpClient);

  create(typeClient: NewTypeClient): Observable<ITypeClient> {
    return this.http.post<ITypeClient>(this.resourceUrl, typeClient);
  }

  update(typeClient: ITypeClient): Observable<ITypeClient> {
    return this.http.put<ITypeClient>(`${this.resourceUrl}/${encodeURIComponent(this.getTypeClientIdentifier(typeClient))}`, typeClient);
  }

  partialUpdate(typeClient: PartialUpdateTypeClient): Observable<ITypeClient> {
    return this.http.patch<ITypeClient>(`${this.resourceUrl}/${encodeURIComponent(this.getTypeClientIdentifier(typeClient))}`, typeClient);
  }

  find(id: string): Observable<ITypeClient> {
    return this.http.get<ITypeClient>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITypeClient[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITypeClient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTypeClientIdentifier(typeClient: Pick<ITypeClient, 'id'>): string {
    return typeClient.id;
  }

  compareTypeClient(o1: Pick<ITypeClient, 'id'> | null, o2: Pick<ITypeClient, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeClientIdentifier(o1) === this.getTypeClientIdentifier(o2) : o1 === o2;
  }

  addTypeClientToCollectionIfMissing<Type extends Pick<ITypeClient, 'id'>>(
    typeClientCollection: Type[],
    ...typeClientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeClients: Type[] = typeClientsToCheck.filter(typeClientItem => typeClientItem !== null && typeClientItem !== undefined);
    if (typeClients.length > 0) {
      const typeClientCollectionIdentifiers = typeClientCollection.map(typeClientItem => this.getTypeClientIdentifier(typeClientItem));
      const typeClientsToAdd = typeClients.filter(typeClientItem => {
        const typeClientIdentifier = this.getTypeClientIdentifier(typeClientItem);
        if (typeClientCollectionIdentifiers.includes(typeClientIdentifier)) {
          return false;
        }
        typeClientCollectionIdentifiers.push(typeClientIdentifier);
        return true;
      });
      return [...typeClientsToAdd, ...typeClientCollection];
    }
    return typeClientCollection;
  }
}
