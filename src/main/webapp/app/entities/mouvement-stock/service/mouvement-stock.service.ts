import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IMouvementStock, NewMouvementStock } from '../mouvement-stock.model';

export type PartialUpdateMouvementStock = Partial<IMouvementStock> & Pick<IMouvementStock, 'id'>;

type RestOf<T extends IMouvementStock | NewMouvementStock> = Omit<T, 'dateTransaction'> & {
  dateTransaction?: string | null;
};

export type RestMouvementStock = RestOf<IMouvementStock>;

export type NewRestMouvementStock = RestOf<NewMouvementStock>;

export type PartialUpdateRestMouvementStock = RestOf<PartialUpdateMouvementStock>;

@Service()
export class MouvementStocksService {
  readonly mouvementStocksParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly mouvementStocksResource = httpResource<RestMouvementStock[]>(() => {
    const params = this.mouvementStocksParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of mouvementStock that have been fetched. It is updated when the mouvementStocksResource emits a new value.
   * In case of error while fetching the mouvementStocks, the signal is set to an empty array.
   */
  readonly mouvementStocks = computed(() =>
    (this.mouvementStocksResource.hasValue() ? this.mouvementStocksResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/mouvement-stocks`;

  protected convertValueFromServer(restMouvementStock: RestMouvementStock): IMouvementStock {
    return {
      ...restMouvementStock,
      dateTransaction: restMouvementStock.dateTransaction ? dayjs(restMouvementStock.dateTransaction) : undefined,
    };
  }
}

@Service()
export class MouvementStockService extends MouvementStocksService {
  protected readonly http = inject(HttpClient);

  create(mouvementStock: NewMouvementStock): Observable<IMouvementStock> {
    const copy = this.convertValueFromClient(mouvementStock);
    return this.http.post<RestMouvementStock>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(mouvementStock: IMouvementStock): Observable<IMouvementStock> {
    const copy = this.convertValueFromClient(mouvementStock);
    return this.http
      .put<RestMouvementStock>(`${this.resourceUrl}/${encodeURIComponent(this.getMouvementStockIdentifier(mouvementStock))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(mouvementStock: PartialUpdateMouvementStock): Observable<IMouvementStock> {
    const copy = this.convertValueFromClient(mouvementStock);
    return this.http
      .patch<RestMouvementStock>(`${this.resourceUrl}/${encodeURIComponent(this.getMouvementStockIdentifier(mouvementStock))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IMouvementStock> {
    return this.http
      .get<RestMouvementStock>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IMouvementStock[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMouvementStock[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getMouvementStockIdentifier(mouvementStock: Pick<IMouvementStock, 'id'>): string {
    return mouvementStock.id;
  }

  compareMouvementStock(o1: Pick<IMouvementStock, 'id'> | null, o2: Pick<IMouvementStock, 'id'> | null): boolean {
    return o1 && o2 ? this.getMouvementStockIdentifier(o1) === this.getMouvementStockIdentifier(o2) : o1 === o2;
  }

  addMouvementStockToCollectionIfMissing<Type extends Pick<IMouvementStock, 'id'>>(
    mouvementStockCollection: Type[],
    ...mouvementStocksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mouvementStocks: Type[] = mouvementStocksToCheck.filter(
      mouvementStockItem => mouvementStockItem !== null && mouvementStockItem !== undefined,
    );
    if (mouvementStocks.length > 0) {
      const mouvementStockCollectionIdentifiers = mouvementStockCollection.map(mouvementStockItem =>
        this.getMouvementStockIdentifier(mouvementStockItem),
      );
      const mouvementStocksToAdd = mouvementStocks.filter(mouvementStockItem => {
        const mouvementStockIdentifier = this.getMouvementStockIdentifier(mouvementStockItem);
        if (mouvementStockCollectionIdentifiers.includes(mouvementStockIdentifier)) {
          return false;
        }
        mouvementStockCollectionIdentifiers.push(mouvementStockIdentifier);
        return true;
      });
      return [...mouvementStocksToAdd, ...mouvementStockCollection];
    }
    return mouvementStockCollection;
  }

  protected convertValueFromClient<T extends IMouvementStock | NewMouvementStock | PartialUpdateMouvementStock>(
    mouvementStock: T,
  ): RestOf<T> {
    return {
      ...mouvementStock,
      dateTransaction: mouvementStock.dateTransaction?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestMouvementStock): IMouvementStock {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestMouvementStock[]): IMouvementStock[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
