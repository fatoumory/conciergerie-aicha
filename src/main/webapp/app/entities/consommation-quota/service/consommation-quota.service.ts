import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IConsommationQuota, NewConsommationQuota } from '../consommation-quota.model';

export type PartialUpdateConsommationQuota = Partial<IConsommationQuota> & Pick<IConsommationQuota, 'id'>;

type RestOf<T extends IConsommationQuota | NewConsommationQuota> = Omit<T, 'dateConsommation'> & {
  dateConsommation?: string | null;
};

export type RestConsommationQuota = RestOf<IConsommationQuota>;

export type NewRestConsommationQuota = RestOf<NewConsommationQuota>;

export type PartialUpdateRestConsommationQuota = RestOf<PartialUpdateConsommationQuota>;

@Service()
export class ConsommationQuotasService {
  readonly consommationQuotasParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly consommationQuotasResource = httpResource<RestConsommationQuota[]>(() => {
    const params = this.consommationQuotasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of consommationQuota that have been fetched. It is updated when the consommationQuotasResource emits a new value.
   * In case of error while fetching the consommationQuotas, the signal is set to an empty array.
   */
  readonly consommationQuotas = computed(() =>
    (this.consommationQuotasResource.hasValue() ? this.consommationQuotasResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/consommation-quotas`;

  protected convertValueFromServer(restConsommationQuota: RestConsommationQuota): IConsommationQuota {
    return {
      ...restConsommationQuota,
      dateConsommation: restConsommationQuota.dateConsommation ? dayjs(restConsommationQuota.dateConsommation) : undefined,
    };
  }
}

@Service()
export class ConsommationQuotaService extends ConsommationQuotasService {
  protected readonly http = inject(HttpClient);

  create(consommationQuota: NewConsommationQuota): Observable<IConsommationQuota> {
    const copy = this.convertValueFromClient(consommationQuota);
    return this.http.post<RestConsommationQuota>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(consommationQuota: IConsommationQuota): Observable<IConsommationQuota> {
    const copy = this.convertValueFromClient(consommationQuota);
    return this.http
      .put<RestConsommationQuota>(`${this.resourceUrl}/${encodeURIComponent(this.getConsommationQuotaIdentifier(consommationQuota))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(consommationQuota: PartialUpdateConsommationQuota): Observable<IConsommationQuota> {
    const copy = this.convertValueFromClient(consommationQuota);
    return this.http
      .patch<RestConsommationQuota>(
        `${this.resourceUrl}/${encodeURIComponent(this.getConsommationQuotaIdentifier(consommationQuota))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IConsommationQuota> {
    return this.http
      .get<RestConsommationQuota>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IConsommationQuota[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConsommationQuota[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getConsommationQuotaIdentifier(consommationQuota: Pick<IConsommationQuota, 'id'>): string {
    return consommationQuota.id;
  }

  compareConsommationQuota(o1: Pick<IConsommationQuota, 'id'> | null, o2: Pick<IConsommationQuota, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsommationQuotaIdentifier(o1) === this.getConsommationQuotaIdentifier(o2) : o1 === o2;
  }

  addConsommationQuotaToCollectionIfMissing<Type extends Pick<IConsommationQuota, 'id'>>(
    consommationQuotaCollection: Type[],
    ...consommationQuotasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consommationQuotas: Type[] = consommationQuotasToCheck.filter(
      consommationQuotaItem => consommationQuotaItem !== null && consommationQuotaItem !== undefined,
    );
    if (consommationQuotas.length > 0) {
      const consommationQuotaCollectionIdentifiers = consommationQuotaCollection.map(consommationQuotaItem =>
        this.getConsommationQuotaIdentifier(consommationQuotaItem),
      );
      const consommationQuotasToAdd = consommationQuotas.filter(consommationQuotaItem => {
        const consommationQuotaIdentifier = this.getConsommationQuotaIdentifier(consommationQuotaItem);
        if (consommationQuotaCollectionIdentifiers.includes(consommationQuotaIdentifier)) {
          return false;
        }
        consommationQuotaCollectionIdentifiers.push(consommationQuotaIdentifier);
        return true;
      });
      return [...consommationQuotasToAdd, ...consommationQuotaCollection];
    }
    return consommationQuotaCollection;
  }

  protected convertValueFromClient<T extends IConsommationQuota | NewConsommationQuota | PartialUpdateConsommationQuota>(
    consommationQuota: T,
  ): RestOf<T> {
    return {
      ...consommationQuota,
      dateConsommation: consommationQuota.dateConsommation?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestConsommationQuota): IConsommationQuota {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestConsommationQuota[]): IConsommationQuota[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
