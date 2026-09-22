import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IAffectationDemande, NewAffectationDemande } from '../affectation-demande.model';

export type PartialUpdateAffectationDemande = Partial<IAffectationDemande> & Pick<IAffectationDemande, 'id'>;

type RestOf<T extends IAffectationDemande | NewAffectationDemande> = Omit<T, 'dateAffectation'> & {
  dateAffectation?: string | null;
};

export type RestAffectationDemande = RestOf<IAffectationDemande>;

export type NewRestAffectationDemande = RestOf<NewAffectationDemande>;

export type PartialUpdateRestAffectationDemande = RestOf<PartialUpdateAffectationDemande>;

@Service()
export class AffectationDemandesService {
  readonly affectationDemandesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly affectationDemandesResource = httpResource<RestAffectationDemande[]>(() => {
    const params = this.affectationDemandesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of affectationDemande that have been fetched. It is updated when the affectationDemandesResource emits a new value.
   * In case of error while fetching the affectationDemandes, the signal is set to an empty array.
   */
  readonly affectationDemandes = computed(() =>
    (this.affectationDemandesResource.hasValue() ? this.affectationDemandesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/affectation-demandes`;

  protected convertValueFromServer(restAffectationDemande: RestAffectationDemande): IAffectationDemande {
    return {
      ...restAffectationDemande,
      dateAffectation: restAffectationDemande.dateAffectation ? dayjs(restAffectationDemande.dateAffectation) : undefined,
    };
  }
}

@Service()
export class AffectationDemandeService extends AffectationDemandesService {
  protected readonly http = inject(HttpClient);

  create(affectationDemande: NewAffectationDemande): Observable<IAffectationDemande> {
    const copy = this.convertValueFromClient(affectationDemande);
    return this.http.post<RestAffectationDemande>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(affectationDemande: IAffectationDemande): Observable<IAffectationDemande> {
    const copy = this.convertValueFromClient(affectationDemande);
    return this.http
      .put<RestAffectationDemande>(
        `${this.resourceUrl}/${encodeURIComponent(this.getAffectationDemandeIdentifier(affectationDemande))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(affectationDemande: PartialUpdateAffectationDemande): Observable<IAffectationDemande> {
    const copy = this.convertValueFromClient(affectationDemande);
    return this.http
      .patch<RestAffectationDemande>(
        `${this.resourceUrl}/${encodeURIComponent(this.getAffectationDemandeIdentifier(affectationDemande))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IAffectationDemande> {
    return this.http
      .get<RestAffectationDemande>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IAffectationDemande[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAffectationDemande[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAffectationDemandeIdentifier(affectationDemande: Pick<IAffectationDemande, 'id'>): string {
    return affectationDemande.id;
  }

  compareAffectationDemande(o1: Pick<IAffectationDemande, 'id'> | null, o2: Pick<IAffectationDemande, 'id'> | null): boolean {
    return o1 && o2 ? this.getAffectationDemandeIdentifier(o1) === this.getAffectationDemandeIdentifier(o2) : o1 === o2;
  }

  addAffectationDemandeToCollectionIfMissing<Type extends Pick<IAffectationDemande, 'id'>>(
    affectationDemandeCollection: Type[],
    ...affectationDemandesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const affectationDemandes: Type[] = affectationDemandesToCheck.filter(
      affectationDemandeItem => affectationDemandeItem !== null && affectationDemandeItem !== undefined,
    );
    if (affectationDemandes.length > 0) {
      const affectationDemandeCollectionIdentifiers = affectationDemandeCollection.map(affectationDemandeItem =>
        this.getAffectationDemandeIdentifier(affectationDemandeItem),
      );
      const affectationDemandesToAdd = affectationDemandes.filter(affectationDemandeItem => {
        const affectationDemandeIdentifier = this.getAffectationDemandeIdentifier(affectationDemandeItem);
        if (affectationDemandeCollectionIdentifiers.includes(affectationDemandeIdentifier)) {
          return false;
        }
        affectationDemandeCollectionIdentifiers.push(affectationDemandeIdentifier);
        return true;
      });
      return [...affectationDemandesToAdd, ...affectationDemandeCollection];
    }
    return affectationDemandeCollection;
  }

  protected convertValueFromClient<T extends IAffectationDemande | NewAffectationDemande | PartialUpdateAffectationDemande>(
    affectationDemande: T,
  ): RestOf<T> {
    return {
      ...affectationDemande,
      dateAffectation: affectationDemande.dateAffectation?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestAffectationDemande): IAffectationDemande {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestAffectationDemande[]): IAffectationDemande[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
