import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITransactionPaiement, NewTransactionPaiement } from '../transaction-paiement.model';

export type PartialUpdateTransactionPaiement = Partial<ITransactionPaiement> & Pick<ITransactionPaiement, 'id'>;

type RestOf<T extends ITransactionPaiement | NewTransactionPaiement> = Omit<T, 'dateTransaction'> & {
  dateTransaction?: string | null;
};

export type RestTransactionPaiement = RestOf<ITransactionPaiement>;

export type NewRestTransactionPaiement = RestOf<NewTransactionPaiement>;

export type PartialUpdateRestTransactionPaiement = RestOf<PartialUpdateTransactionPaiement>;

@Service()
export class TransactionPaiementsService {
  readonly transactionPaiementsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly transactionPaiementsResource = httpResource<RestTransactionPaiement[]>(() => {
    const params = this.transactionPaiementsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of transactionPaiement that have been fetched. It is updated when the transactionPaiementsResource emits a new value.
   * In case of error while fetching the transactionPaiements, the signal is set to an empty array.
   */
  readonly transactionPaiements = computed(() =>
    (this.transactionPaiementsResource.hasValue() ? this.transactionPaiementsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/transaction-paiements`;

  protected convertValueFromServer(restTransactionPaiement: RestTransactionPaiement): ITransactionPaiement {
    return {
      ...restTransactionPaiement,
      dateTransaction: restTransactionPaiement.dateTransaction ? dayjs(restTransactionPaiement.dateTransaction) : undefined,
    };
  }
}

@Service()
export class TransactionPaiementService extends TransactionPaiementsService {
  protected readonly http = inject(HttpClient);

  create(transactionPaiement: NewTransactionPaiement): Observable<ITransactionPaiement> {
    const copy = this.convertValueFromClient(transactionPaiement);
    return this.http.post<RestTransactionPaiement>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transactionPaiement: ITransactionPaiement): Observable<ITransactionPaiement> {
    const copy = this.convertValueFromClient(transactionPaiement);
    return this.http
      .put<RestTransactionPaiement>(
        `${this.resourceUrl}/${encodeURIComponent(this.getTransactionPaiementIdentifier(transactionPaiement))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transactionPaiement: PartialUpdateTransactionPaiement): Observable<ITransactionPaiement> {
    const copy = this.convertValueFromClient(transactionPaiement);
    return this.http
      .patch<RestTransactionPaiement>(
        `${this.resourceUrl}/${encodeURIComponent(this.getTransactionPaiementIdentifier(transactionPaiement))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<ITransactionPaiement> {
    return this.http
      .get<RestTransactionPaiement>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ITransactionPaiement[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransactionPaiement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTransactionPaiementIdentifier(transactionPaiement: Pick<ITransactionPaiement, 'id'>): string {
    return transactionPaiement.id;
  }

  compareTransactionPaiement(o1: Pick<ITransactionPaiement, 'id'> | null, o2: Pick<ITransactionPaiement, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransactionPaiementIdentifier(o1) === this.getTransactionPaiementIdentifier(o2) : o1 === o2;
  }

  addTransactionPaiementToCollectionIfMissing<Type extends Pick<ITransactionPaiement, 'id'>>(
    transactionPaiementCollection: Type[],
    ...transactionPaiementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transactionPaiements: Type[] = transactionPaiementsToCheck.filter(
      transactionPaiementItem => transactionPaiementItem !== null && transactionPaiementItem !== undefined,
    );
    if (transactionPaiements.length > 0) {
      const transactionPaiementCollectionIdentifiers = transactionPaiementCollection.map(transactionPaiementItem =>
        this.getTransactionPaiementIdentifier(transactionPaiementItem),
      );
      const transactionPaiementsToAdd = transactionPaiements.filter(transactionPaiementItem => {
        const transactionPaiementIdentifier = this.getTransactionPaiementIdentifier(transactionPaiementItem);
        if (transactionPaiementCollectionIdentifiers.includes(transactionPaiementIdentifier)) {
          return false;
        }
        transactionPaiementCollectionIdentifiers.push(transactionPaiementIdentifier);
        return true;
      });
      return [...transactionPaiementsToAdd, ...transactionPaiementCollection];
    }
    return transactionPaiementCollection;
  }

  protected convertValueFromClient<T extends ITransactionPaiement | NewTransactionPaiement | PartialUpdateTransactionPaiement>(
    transactionPaiement: T,
  ): RestOf<T> {
    return {
      ...transactionPaiement,
      dateTransaction: transactionPaiement.dateTransaction?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestTransactionPaiement): ITransactionPaiement {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestTransactionPaiement[]): ITransactionPaiement[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
