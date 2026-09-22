import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPrestation, NewPrestation } from '../prestation.model';

export type PartialUpdatePrestation = Partial<IPrestation> & Pick<IPrestation, 'id'>;

type RestOf<T extends IPrestation | NewPrestation> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestPrestation = RestOf<IPrestation>;

export type NewRestPrestation = RestOf<NewPrestation>;

export type PartialUpdateRestPrestation = RestOf<PartialUpdatePrestation>;

@Service()
export class PrestationsService {
  readonly prestationsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly prestationsResource = httpResource<RestPrestation[]>(() => {
    const params = this.prestationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of prestation that have been fetched. It is updated when the prestationsResource emits a new value.
   * In case of error while fetching the prestations, the signal is set to an empty array.
   */
  readonly prestations = computed(() =>
    (this.prestationsResource.hasValue() ? this.prestationsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/prestations`;

  protected convertValueFromServer(restPrestation: RestPrestation): IPrestation {
    return {
      ...restPrestation,
      dateDebut: restPrestation.dateDebut ? dayjs(restPrestation.dateDebut) : undefined,
      dateFin: restPrestation.dateFin ? dayjs(restPrestation.dateFin) : undefined,
    };
  }
}

@Service()
export class PrestationService extends PrestationsService {
  protected readonly http = inject(HttpClient);

  create(prestation: NewPrestation): Observable<IPrestation> {
    const copy = this.convertValueFromClient(prestation);
    return this.http.post<RestPrestation>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(prestation: IPrestation): Observable<IPrestation> {
    const copy = this.convertValueFromClient(prestation);
    return this.http
      .put<RestPrestation>(`${this.resourceUrl}/${encodeURIComponent(this.getPrestationIdentifier(prestation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(prestation: PartialUpdatePrestation): Observable<IPrestation> {
    const copy = this.convertValueFromClient(prestation);
    return this.http
      .patch<RestPrestation>(`${this.resourceUrl}/${encodeURIComponent(this.getPrestationIdentifier(prestation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IPrestation> {
    return this.http
      .get<RestPrestation>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPrestation[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPrestation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPrestationIdentifier(prestation: Pick<IPrestation, 'id'>): string {
    return prestation.id;
  }

  comparePrestation(o1: Pick<IPrestation, 'id'> | null, o2: Pick<IPrestation, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrestationIdentifier(o1) === this.getPrestationIdentifier(o2) : o1 === o2;
  }

  addPrestationToCollectionIfMissing<Type extends Pick<IPrestation, 'id'>>(
    prestationCollection: Type[],
    ...prestationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prestations: Type[] = prestationsToCheck.filter(prestationItem => prestationItem !== null && prestationItem !== undefined);
    if (prestations.length > 0) {
      const prestationCollectionIdentifiers = prestationCollection.map(prestationItem => this.getPrestationIdentifier(prestationItem));
      const prestationsToAdd = prestations.filter(prestationItem => {
        const prestationIdentifier = this.getPrestationIdentifier(prestationItem);
        if (prestationCollectionIdentifiers.includes(prestationIdentifier)) {
          return false;
        }
        prestationCollectionIdentifiers.push(prestationIdentifier);
        return true;
      });
      return [...prestationsToAdd, ...prestationCollection];
    }
    return prestationCollection;
  }

  protected convertValueFromClient<T extends IPrestation | NewPrestation | PartialUpdatePrestation>(prestation: T): RestOf<T> {
    return {
      ...prestation,
      dateDebut: prestation.dateDebut?.toJSON() ?? null,
      dateFin: prestation.dateFin?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPrestation): IPrestation {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPrestation[]): IPrestation[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
