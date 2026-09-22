import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICouverturePartenaire, NewCouverturePartenaire } from '../couverture-partenaire.model';

export type PartialUpdateCouverturePartenaire = Partial<ICouverturePartenaire> & Pick<ICouverturePartenaire, 'id'>;

type RestOf<T extends ICouverturePartenaire | NewCouverturePartenaire> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestCouverturePartenaire = RestOf<ICouverturePartenaire>;

export type NewRestCouverturePartenaire = RestOf<NewCouverturePartenaire>;

export type PartialUpdateRestCouverturePartenaire = RestOf<PartialUpdateCouverturePartenaire>;

@Service()
export class CouverturePartenairesService {
  readonly couverturePartenairesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly couverturePartenairesResource = httpResource<RestCouverturePartenaire[]>(() => {
    const params = this.couverturePartenairesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of couverturePartenaire that have been fetched. It is updated when the couverturePartenairesResource emits a new value.
   * In case of error while fetching the couverturePartenaires, the signal is set to an empty array.
   */
  readonly couverturePartenaires = computed(() =>
    (this.couverturePartenairesResource.hasValue() ? this.couverturePartenairesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/couverture-partenaires`;

  protected convertValueFromServer(restCouverturePartenaire: RestCouverturePartenaire): ICouverturePartenaire {
    return {
      ...restCouverturePartenaire,
      dateDebut: restCouverturePartenaire.dateDebut ? dayjs(restCouverturePartenaire.dateDebut) : undefined,
      dateFin: restCouverturePartenaire.dateFin ? dayjs(restCouverturePartenaire.dateFin) : undefined,
    };
  }
}

@Service()
export class CouverturePartenaireService extends CouverturePartenairesService {
  protected readonly http = inject(HttpClient);

  create(couverturePartenaire: NewCouverturePartenaire): Observable<ICouverturePartenaire> {
    const copy = this.convertValueFromClient(couverturePartenaire);
    return this.http.post<RestCouverturePartenaire>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(couverturePartenaire: ICouverturePartenaire): Observable<ICouverturePartenaire> {
    const copy = this.convertValueFromClient(couverturePartenaire);
    return this.http
      .put<RestCouverturePartenaire>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCouverturePartenaireIdentifier(couverturePartenaire))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(couverturePartenaire: PartialUpdateCouverturePartenaire): Observable<ICouverturePartenaire> {
    const copy = this.convertValueFromClient(couverturePartenaire);
    return this.http
      .patch<RestCouverturePartenaire>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCouverturePartenaireIdentifier(couverturePartenaire))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<ICouverturePartenaire> {
    return this.http
      .get<RestCouverturePartenaire>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICouverturePartenaire[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCouverturePartenaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCouverturePartenaireIdentifier(couverturePartenaire: Pick<ICouverturePartenaire, 'id'>): string {
    return couverturePartenaire.id;
  }

  compareCouverturePartenaire(o1: Pick<ICouverturePartenaire, 'id'> | null, o2: Pick<ICouverturePartenaire, 'id'> | null): boolean {
    return o1 && o2 ? this.getCouverturePartenaireIdentifier(o1) === this.getCouverturePartenaireIdentifier(o2) : o1 === o2;
  }

  addCouverturePartenaireToCollectionIfMissing<Type extends Pick<ICouverturePartenaire, 'id'>>(
    couverturePartenaireCollection: Type[],
    ...couverturePartenairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const couverturePartenaires: Type[] = couverturePartenairesToCheck.filter(
      couverturePartenaireItem => couverturePartenaireItem !== null && couverturePartenaireItem !== undefined,
    );
    if (couverturePartenaires.length > 0) {
      const couverturePartenaireCollectionIdentifiers = couverturePartenaireCollection.map(couverturePartenaireItem =>
        this.getCouverturePartenaireIdentifier(couverturePartenaireItem),
      );
      const couverturePartenairesToAdd = couverturePartenaires.filter(couverturePartenaireItem => {
        const couverturePartenaireIdentifier = this.getCouverturePartenaireIdentifier(couverturePartenaireItem);
        if (couverturePartenaireCollectionIdentifiers.includes(couverturePartenaireIdentifier)) {
          return false;
        }
        couverturePartenaireCollectionIdentifiers.push(couverturePartenaireIdentifier);
        return true;
      });
      return [...couverturePartenairesToAdd, ...couverturePartenaireCollection];
    }
    return couverturePartenaireCollection;
  }

  protected convertValueFromClient<T extends ICouverturePartenaire | NewCouverturePartenaire | PartialUpdateCouverturePartenaire>(
    couverturePartenaire: T,
  ): RestOf<T> {
    return {
      ...couverturePartenaire,
      dateDebut: couverturePartenaire.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: couverturePartenaire.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCouverturePartenaire): ICouverturePartenaire {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCouverturePartenaire[]): ICouverturePartenaire[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
