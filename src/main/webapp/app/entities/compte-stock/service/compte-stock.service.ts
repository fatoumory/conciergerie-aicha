import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICompteStock, NewCompteStock } from '../compte-stock.model';

export type PartialUpdateCompteStock = Partial<ICompteStock> & Pick<ICompteStock, 'id'>;

@Service()
export class CompteStocksService {
  readonly compteStocksParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly compteStocksResource = httpResource<ICompteStock[]>(() => {
    const params = this.compteStocksParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of compteStock that have been fetched. It is updated when the compteStocksResource emits a new value.
   * In case of error while fetching the compteStocks, the signal is set to an empty array.
   */
  readonly compteStocks = computed(() => (this.compteStocksResource.hasValue() ? this.compteStocksResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/compte-stocks`;
}

@Service()
export class CompteStockService extends CompteStocksService {
  protected readonly http = inject(HttpClient);

  create(compteStock: NewCompteStock): Observable<ICompteStock> {
    return this.http.post<ICompteStock>(this.resourceUrl, compteStock);
  }

  update(compteStock: ICompteStock): Observable<ICompteStock> {
    return this.http.put<ICompteStock>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCompteStockIdentifier(compteStock))}`,
      compteStock,
    );
  }

  partialUpdate(compteStock: PartialUpdateCompteStock): Observable<ICompteStock> {
    return this.http.patch<ICompteStock>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCompteStockIdentifier(compteStock))}`,
      compteStock,
    );
  }

  find(id: string): Observable<ICompteStock> {
    return this.http.get<ICompteStock>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICompteStock[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICompteStock[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCompteStockIdentifier(compteStock: Pick<ICompteStock, 'id'>): string {
    return compteStock.id;
  }

  compareCompteStock(o1: Pick<ICompteStock, 'id'> | null, o2: Pick<ICompteStock, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompteStockIdentifier(o1) === this.getCompteStockIdentifier(o2) : o1 === o2;
  }

  addCompteStockToCollectionIfMissing<Type extends Pick<ICompteStock, 'id'>>(
    compteStockCollection: Type[],
    ...compteStocksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compteStocks: Type[] = compteStocksToCheck.filter(compteStockItem => compteStockItem !== null && compteStockItem !== undefined);
    if (compteStocks.length > 0) {
      const compteStockCollectionIdentifiers = compteStockCollection.map(compteStockItem => this.getCompteStockIdentifier(compteStockItem));
      const compteStocksToAdd = compteStocks.filter(compteStockItem => {
        const compteStockIdentifier = this.getCompteStockIdentifier(compteStockItem);
        if (compteStockCollectionIdentifiers.includes(compteStockIdentifier)) {
          return false;
        }
        compteStockCollectionIdentifiers.push(compteStockIdentifier);
        return true;
      });
      return [...compteStocksToAdd, ...compteStockCollection];
    }
    return compteStockCollection;
  }
}
