import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFacture, NewFacture } from '../facture.model';

export type PartialUpdateFacture = Partial<IFacture> & Pick<IFacture, 'id'>;

type RestOf<T extends IFacture | NewFacture> = Omit<T, 'dateGeneration'> & {
  dateGeneration?: string | null;
};

export type RestFacture = RestOf<IFacture>;

export type NewRestFacture = RestOf<NewFacture>;

export type PartialUpdateRestFacture = RestOf<PartialUpdateFacture>;

@Service()
export class FacturesService {
  readonly facturesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly facturesResource = httpResource<RestFacture[]>(() => {
    const params = this.facturesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of facture that have been fetched. It is updated when the facturesResource emits a new value.
   * In case of error while fetching the factures, the signal is set to an empty array.
   */
  readonly factures = computed(() =>
    (this.facturesResource.hasValue() ? this.facturesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/factures`;

  protected convertValueFromServer(restFacture: RestFacture): IFacture {
    return {
      ...restFacture,
      dateGeneration: restFacture.dateGeneration ? dayjs(restFacture.dateGeneration) : undefined,
    };
  }
}

@Service()
export class FactureService extends FacturesService {
  protected readonly http = inject(HttpClient);

  create(facture: NewFacture): Observable<IFacture> {
    const copy = this.convertValueFromClient(facture);
    return this.http.post<RestFacture>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(facture: IFacture): Observable<IFacture> {
    const copy = this.convertValueFromClient(facture);
    return this.http
      .put<RestFacture>(`${this.resourceUrl}/${encodeURIComponent(this.getFactureIdentifier(facture))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(facture: PartialUpdateFacture): Observable<IFacture> {
    const copy = this.convertValueFromClient(facture);
    return this.http
      .patch<RestFacture>(`${this.resourceUrl}/${encodeURIComponent(this.getFactureIdentifier(facture))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IFacture> {
    return this.http
      .get<RestFacture>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFacture[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFacture[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFactureIdentifier(facture: Pick<IFacture, 'id'>): string {
    return facture.id;
  }

  compareFacture(o1: Pick<IFacture, 'id'> | null, o2: Pick<IFacture, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactureIdentifier(o1) === this.getFactureIdentifier(o2) : o1 === o2;
  }

  addFactureToCollectionIfMissing<Type extends Pick<IFacture, 'id'>>(
    factureCollection: Type[],
    ...facturesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factures: Type[] = facturesToCheck.filter(factureItem => factureItem !== null && factureItem !== undefined);
    if (factures.length > 0) {
      const factureCollectionIdentifiers = factureCollection.map(factureItem => this.getFactureIdentifier(factureItem));
      const facturesToAdd = factures.filter(factureItem => {
        const factureIdentifier = this.getFactureIdentifier(factureItem);
        if (factureCollectionIdentifiers.includes(factureIdentifier)) {
          return false;
        }
        factureCollectionIdentifiers.push(factureIdentifier);
        return true;
      });
      return [...facturesToAdd, ...factureCollection];
    }
    return factureCollection;
  }

  protected convertValueFromClient<T extends IFacture | NewFacture | PartialUpdateFacture>(facture: T): RestOf<T> {
    return {
      ...facture,
      dateGeneration: facture.dateGeneration?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFacture): IFacture {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFacture[]): IFacture[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
