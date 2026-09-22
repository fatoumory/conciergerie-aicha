import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICodePromo, NewCodePromo } from '../code-promo.model';

export type PartialUpdateCodePromo = Partial<ICodePromo> & Pick<ICodePromo, 'id'>;

type RestOf<T extends ICodePromo | NewCodePromo> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestCodePromo = RestOf<ICodePromo>;

export type NewRestCodePromo = RestOf<NewCodePromo>;

export type PartialUpdateRestCodePromo = RestOf<PartialUpdateCodePromo>;

@Service()
export class CodePromosService {
  readonly codePromosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly codePromosResource = httpResource<RestCodePromo[]>(() => {
    const params = this.codePromosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of codePromo that have been fetched. It is updated when the codePromosResource emits a new value.
   * In case of error while fetching the codePromos, the signal is set to an empty array.
   */
  readonly codePromos = computed(() =>
    (this.codePromosResource.hasValue() ? this.codePromosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/code-promos`;

  protected convertValueFromServer(restCodePromo: RestCodePromo): ICodePromo {
    return {
      ...restCodePromo,
      dateDebut: restCodePromo.dateDebut ? dayjs(restCodePromo.dateDebut) : undefined,
      dateFin: restCodePromo.dateFin ? dayjs(restCodePromo.dateFin) : undefined,
    };
  }
}

@Service()
export class CodePromoService extends CodePromosService {
  protected readonly http = inject(HttpClient);

  create(codePromo: NewCodePromo): Observable<ICodePromo> {
    const copy = this.convertValueFromClient(codePromo);
    return this.http.post<RestCodePromo>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(codePromo: ICodePromo): Observable<ICodePromo> {
    const copy = this.convertValueFromClient(codePromo);
    return this.http
      .put<RestCodePromo>(`${this.resourceUrl}/${encodeURIComponent(this.getCodePromoIdentifier(codePromo))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(codePromo: PartialUpdateCodePromo): Observable<ICodePromo> {
    const copy = this.convertValueFromClient(codePromo);
    return this.http
      .patch<RestCodePromo>(`${this.resourceUrl}/${encodeURIComponent(this.getCodePromoIdentifier(codePromo))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<ICodePromo> {
    return this.http
      .get<RestCodePromo>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICodePromo[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCodePromo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCodePromoIdentifier(codePromo: Pick<ICodePromo, 'id'>): string {
    return codePromo.id;
  }

  compareCodePromo(o1: Pick<ICodePromo, 'id'> | null, o2: Pick<ICodePromo, 'id'> | null): boolean {
    return o1 && o2 ? this.getCodePromoIdentifier(o1) === this.getCodePromoIdentifier(o2) : o1 === o2;
  }

  addCodePromoToCollectionIfMissing<Type extends Pick<ICodePromo, 'id'>>(
    codePromoCollection: Type[],
    ...codePromosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const codePromos: Type[] = codePromosToCheck.filter(codePromoItem => codePromoItem !== null && codePromoItem !== undefined);
    if (codePromos.length > 0) {
      const codePromoCollectionIdentifiers = codePromoCollection.map(codePromoItem => this.getCodePromoIdentifier(codePromoItem));
      const codePromosToAdd = codePromos.filter(codePromoItem => {
        const codePromoIdentifier = this.getCodePromoIdentifier(codePromoItem);
        if (codePromoCollectionIdentifiers.includes(codePromoIdentifier)) {
          return false;
        }
        codePromoCollectionIdentifiers.push(codePromoIdentifier);
        return true;
      });
      return [...codePromosToAdd, ...codePromoCollection];
    }
    return codePromoCollection;
  }

  protected convertValueFromClient<T extends ICodePromo | NewCodePromo | PartialUpdateCodePromo>(codePromo: T): RestOf<T> {
    return {
      ...codePromo,
      dateDebut: codePromo.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: codePromo.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCodePromo): ICodePromo {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCodePromo[]): ICodePromo[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
