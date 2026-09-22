import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IHistoriqueStatutDemande, NewHistoriqueStatutDemande } from '../historique-statut-demande.model';

export type PartialUpdateHistoriqueStatutDemande = Partial<IHistoriqueStatutDemande> & Pick<IHistoriqueStatutDemande, 'id'>;

type RestOf<T extends IHistoriqueStatutDemande | NewHistoriqueStatutDemande> = Omit<T, 'dateChangement'> & {
  dateChangement?: string | null;
};

export type RestHistoriqueStatutDemande = RestOf<IHistoriqueStatutDemande>;

export type NewRestHistoriqueStatutDemande = RestOf<NewHistoriqueStatutDemande>;

export type PartialUpdateRestHistoriqueStatutDemande = RestOf<PartialUpdateHistoriqueStatutDemande>;

@Service()
export class HistoriqueStatutDemandesService {
  readonly historiqueStatutDemandesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly historiqueStatutDemandesResource = httpResource<RestHistoriqueStatutDemande[]>(() => {
    const params = this.historiqueStatutDemandesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of historiqueStatutDemande that have been fetched. It is updated when the historiqueStatutDemandesResource emits a new value.
   * In case of error while fetching the historiqueStatutDemandes, the signal is set to an empty array.
   */
  readonly historiqueStatutDemandes = computed(() =>
    (this.historiqueStatutDemandesResource.hasValue() ? this.historiqueStatutDemandesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/historique-statut-demandes`;

  protected convertValueFromServer(restHistoriqueStatutDemande: RestHistoriqueStatutDemande): IHistoriqueStatutDemande {
    return {
      ...restHistoriqueStatutDemande,
      dateChangement: restHistoriqueStatutDemande.dateChangement ? dayjs(restHistoriqueStatutDemande.dateChangement) : undefined,
    };
  }
}

@Service()
export class HistoriqueStatutDemandeService extends HistoriqueStatutDemandesService {
  protected readonly http = inject(HttpClient);

  create(historiqueStatutDemande: NewHistoriqueStatutDemande): Observable<IHistoriqueStatutDemande> {
    const copy = this.convertValueFromClient(historiqueStatutDemande);
    return this.http.post<RestHistoriqueStatutDemande>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(historiqueStatutDemande: IHistoriqueStatutDemande): Observable<IHistoriqueStatutDemande> {
    const copy = this.convertValueFromClient(historiqueStatutDemande);
    return this.http
      .put<RestHistoriqueStatutDemande>(
        `${this.resourceUrl}/${encodeURIComponent(this.getHistoriqueStatutDemandeIdentifier(historiqueStatutDemande))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(historiqueStatutDemande: PartialUpdateHistoriqueStatutDemande): Observable<IHistoriqueStatutDemande> {
    const copy = this.convertValueFromClient(historiqueStatutDemande);
    return this.http
      .patch<RestHistoriqueStatutDemande>(
        `${this.resourceUrl}/${encodeURIComponent(this.getHistoriqueStatutDemandeIdentifier(historiqueStatutDemande))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IHistoriqueStatutDemande> {
    return this.http
      .get<RestHistoriqueStatutDemande>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IHistoriqueStatutDemande[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistoriqueStatutDemande[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getHistoriqueStatutDemandeIdentifier(historiqueStatutDemande: Pick<IHistoriqueStatutDemande, 'id'>): string {
    return historiqueStatutDemande.id;
  }

  compareHistoriqueStatutDemande(
    o1: Pick<IHistoriqueStatutDemande, 'id'> | null,
    o2: Pick<IHistoriqueStatutDemande, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getHistoriqueStatutDemandeIdentifier(o1) === this.getHistoriqueStatutDemandeIdentifier(o2) : o1 === o2;
  }

  addHistoriqueStatutDemandeToCollectionIfMissing<Type extends Pick<IHistoriqueStatutDemande, 'id'>>(
    historiqueStatutDemandeCollection: Type[],
    ...historiqueStatutDemandesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historiqueStatutDemandes: Type[] = historiqueStatutDemandesToCheck.filter(
      historiqueStatutDemandeItem => historiqueStatutDemandeItem !== null && historiqueStatutDemandeItem !== undefined,
    );
    if (historiqueStatutDemandes.length > 0) {
      const historiqueStatutDemandeCollectionIdentifiers = historiqueStatutDemandeCollection.map(historiqueStatutDemandeItem =>
        this.getHistoriqueStatutDemandeIdentifier(historiqueStatutDemandeItem),
      );
      const historiqueStatutDemandesToAdd = historiqueStatutDemandes.filter(historiqueStatutDemandeItem => {
        const historiqueStatutDemandeIdentifier = this.getHistoriqueStatutDemandeIdentifier(historiqueStatutDemandeItem);
        if (historiqueStatutDemandeCollectionIdentifiers.includes(historiqueStatutDemandeIdentifier)) {
          return false;
        }
        historiqueStatutDemandeCollectionIdentifiers.push(historiqueStatutDemandeIdentifier);
        return true;
      });
      return [...historiqueStatutDemandesToAdd, ...historiqueStatutDemandeCollection];
    }
    return historiqueStatutDemandeCollection;
  }

  protected convertValueFromClient<T extends IHistoriqueStatutDemande | NewHistoriqueStatutDemande | PartialUpdateHistoriqueStatutDemande>(
    historiqueStatutDemande: T,
  ): RestOf<T> {
    return {
      ...historiqueStatutDemande,
      dateChangement: historiqueStatutDemande.dateChangement?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestHistoriqueStatutDemande): IHistoriqueStatutDemande {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestHistoriqueStatutDemande[]): IHistoriqueStatutDemande[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
