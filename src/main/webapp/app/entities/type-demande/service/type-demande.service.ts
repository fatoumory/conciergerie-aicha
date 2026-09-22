import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITypeDemande, NewTypeDemande } from '../type-demande.model';

export type PartialUpdateTypeDemande = Partial<ITypeDemande> & Pick<ITypeDemande, 'id'>;

@Service()
export class TypeDemandesService {
  readonly typeDemandesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly typeDemandesResource = httpResource<ITypeDemande[]>(() => {
    const params = this.typeDemandesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of typeDemande that have been fetched. It is updated when the typeDemandesResource emits a new value.
   * In case of error while fetching the typeDemandes, the signal is set to an empty array.
   */
  readonly typeDemandes = computed(() => (this.typeDemandesResource.hasValue() ? this.typeDemandesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/type-demandes`;
}

@Service()
export class TypeDemandeService extends TypeDemandesService {
  protected readonly http = inject(HttpClient);

  create(typeDemande: NewTypeDemande): Observable<ITypeDemande> {
    return this.http.post<ITypeDemande>(this.resourceUrl, typeDemande);
  }

  update(typeDemande: ITypeDemande): Observable<ITypeDemande> {
    return this.http.put<ITypeDemande>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTypeDemandeIdentifier(typeDemande))}`,
      typeDemande,
    );
  }

  partialUpdate(typeDemande: PartialUpdateTypeDemande): Observable<ITypeDemande> {
    return this.http.patch<ITypeDemande>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTypeDemandeIdentifier(typeDemande))}`,
      typeDemande,
    );
  }

  find(id: string): Observable<ITypeDemande> {
    return this.http.get<ITypeDemande>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITypeDemande[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITypeDemande[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTypeDemandeIdentifier(typeDemande: Pick<ITypeDemande, 'id'>): string {
    return typeDemande.id;
  }

  compareTypeDemande(o1: Pick<ITypeDemande, 'id'> | null, o2: Pick<ITypeDemande, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeDemandeIdentifier(o1) === this.getTypeDemandeIdentifier(o2) : o1 === o2;
  }

  addTypeDemandeToCollectionIfMissing<Type extends Pick<ITypeDemande, 'id'>>(
    typeDemandeCollection: Type[],
    ...typeDemandesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeDemandes: Type[] = typeDemandesToCheck.filter(typeDemandeItem => typeDemandeItem !== null && typeDemandeItem !== undefined);
    if (typeDemandes.length > 0) {
      const typeDemandeCollectionIdentifiers = typeDemandeCollection.map(typeDemandeItem => this.getTypeDemandeIdentifier(typeDemandeItem));
      const typeDemandesToAdd = typeDemandes.filter(typeDemandeItem => {
        const typeDemandeIdentifier = this.getTypeDemandeIdentifier(typeDemandeItem);
        if (typeDemandeCollectionIdentifiers.includes(typeDemandeIdentifier)) {
          return false;
        }
        typeDemandeCollectionIdentifiers.push(typeDemandeIdentifier);
        return true;
      });
      return [...typeDemandesToAdd, ...typeDemandeCollection];
    }
    return typeDemandeCollection;
  }
}
