import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartenaire, NewPartenaire } from '../partenaire.model';

export type PartialUpdatePartenaire = Partial<IPartenaire> & Pick<IPartenaire, 'id'>;

@Service()
export class PartenairesService {
  readonly partenairesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly partenairesResource = httpResource<IPartenaire[]>(() => {
    const params = this.partenairesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partenaire that have been fetched. It is updated when the partenairesResource emits a new value.
   * In case of error while fetching the partenaires, the signal is set to an empty array.
   */
  readonly partenaires = computed(() => (this.partenairesResource.hasValue() ? this.partenairesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/partenaires`;
}

@Service()
export class PartenaireService extends PartenairesService {
  protected readonly http = inject(HttpClient);

  create(partenaire: NewPartenaire): Observable<IPartenaire> {
    return this.http.post<IPartenaire>(this.resourceUrl, partenaire);
  }

  update(partenaire: IPartenaire): Observable<IPartenaire> {
    return this.http.put<IPartenaire>(`${this.resourceUrl}/${encodeURIComponent(this.getPartenaireIdentifier(partenaire))}`, partenaire);
  }

  partialUpdate(partenaire: PartialUpdatePartenaire): Observable<IPartenaire> {
    return this.http.patch<IPartenaire>(`${this.resourceUrl}/${encodeURIComponent(this.getPartenaireIdentifier(partenaire))}`, partenaire);
  }

  find(id: string): Observable<IPartenaire> {
    return this.http.get<IPartenaire>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartenaire[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartenaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartenaireIdentifier(partenaire: Pick<IPartenaire, 'id'>): string {
    return partenaire.id;
  }

  comparePartenaire(o1: Pick<IPartenaire, 'id'> | null, o2: Pick<IPartenaire, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartenaireIdentifier(o1) === this.getPartenaireIdentifier(o2) : o1 === o2;
  }

  addPartenaireToCollectionIfMissing<Type extends Pick<IPartenaire, 'id'>>(
    partenaireCollection: Type[],
    ...partenairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partenaires: Type[] = partenairesToCheck.filter(partenaireItem => partenaireItem !== null && partenaireItem !== undefined);
    if (partenaires.length > 0) {
      const partenaireCollectionIdentifiers = partenaireCollection.map(partenaireItem => this.getPartenaireIdentifier(partenaireItem));
      const partenairesToAdd = partenaires.filter(partenaireItem => {
        const partenaireIdentifier = this.getPartenaireIdentifier(partenaireItem);
        if (partenaireCollectionIdentifiers.includes(partenaireIdentifier)) {
          return false;
        }
        partenaireCollectionIdentifiers.push(partenaireIdentifier);
        return true;
      });
      return [...partenairesToAdd, ...partenaireCollection];
    }
    return partenaireCollection;
  }
}
