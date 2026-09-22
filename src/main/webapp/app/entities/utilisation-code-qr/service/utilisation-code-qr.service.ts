import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IUtilisationCodeQr, NewUtilisationCodeQr } from '../utilisation-code-qr.model';

export type PartialUpdateUtilisationCodeQr = Partial<IUtilisationCodeQr> & Pick<IUtilisationCodeQr, 'id'>;

type RestOf<T extends IUtilisationCodeQr | NewUtilisationCodeQr> = Omit<T, 'dateUtilisation'> & {
  dateUtilisation?: string | null;
};

export type RestUtilisationCodeQr = RestOf<IUtilisationCodeQr>;

export type NewRestUtilisationCodeQr = RestOf<NewUtilisationCodeQr>;

export type PartialUpdateRestUtilisationCodeQr = RestOf<PartialUpdateUtilisationCodeQr>;

@Service()
export class UtilisationCodeQrsService {
  readonly utilisationCodeQrsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly utilisationCodeQrsResource = httpResource<RestUtilisationCodeQr[]>(() => {
    const params = this.utilisationCodeQrsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of utilisationCodeQr that have been fetched. It is updated when the utilisationCodeQrsResource emits a new value.
   * In case of error while fetching the utilisationCodeQrs, the signal is set to an empty array.
   */
  readonly utilisationCodeQrs = computed(() =>
    (this.utilisationCodeQrsResource.hasValue() ? this.utilisationCodeQrsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/utilisation-code-qrs`;

  protected convertValueFromServer(restUtilisationCodeQr: RestUtilisationCodeQr): IUtilisationCodeQr {
    return {
      ...restUtilisationCodeQr,
      dateUtilisation: restUtilisationCodeQr.dateUtilisation ? dayjs(restUtilisationCodeQr.dateUtilisation) : undefined,
    };
  }
}

@Service()
export class UtilisationCodeQrService extends UtilisationCodeQrsService {
  protected readonly http = inject(HttpClient);

  create(utilisationCodeQr: NewUtilisationCodeQr): Observable<IUtilisationCodeQr> {
    const copy = this.convertValueFromClient(utilisationCodeQr);
    return this.http.post<RestUtilisationCodeQr>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(utilisationCodeQr: IUtilisationCodeQr): Observable<IUtilisationCodeQr> {
    const copy = this.convertValueFromClient(utilisationCodeQr);
    return this.http
      .put<RestUtilisationCodeQr>(`${this.resourceUrl}/${encodeURIComponent(this.getUtilisationCodeQrIdentifier(utilisationCodeQr))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(utilisationCodeQr: PartialUpdateUtilisationCodeQr): Observable<IUtilisationCodeQr> {
    const copy = this.convertValueFromClient(utilisationCodeQr);
    return this.http
      .patch<RestUtilisationCodeQr>(
        `${this.resourceUrl}/${encodeURIComponent(this.getUtilisationCodeQrIdentifier(utilisationCodeQr))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IUtilisationCodeQr> {
    return this.http
      .get<RestUtilisationCodeQr>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IUtilisationCodeQr[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUtilisationCodeQr[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getUtilisationCodeQrIdentifier(utilisationCodeQr: Pick<IUtilisationCodeQr, 'id'>): string {
    return utilisationCodeQr.id;
  }

  compareUtilisationCodeQr(o1: Pick<IUtilisationCodeQr, 'id'> | null, o2: Pick<IUtilisationCodeQr, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtilisationCodeQrIdentifier(o1) === this.getUtilisationCodeQrIdentifier(o2) : o1 === o2;
  }

  addUtilisationCodeQrToCollectionIfMissing<Type extends Pick<IUtilisationCodeQr, 'id'>>(
    utilisationCodeQrCollection: Type[],
    ...utilisationCodeQrsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utilisationCodeQrs: Type[] = utilisationCodeQrsToCheck.filter(
      utilisationCodeQrItem => utilisationCodeQrItem !== null && utilisationCodeQrItem !== undefined,
    );
    if (utilisationCodeQrs.length > 0) {
      const utilisationCodeQrCollectionIdentifiers = utilisationCodeQrCollection.map(utilisationCodeQrItem =>
        this.getUtilisationCodeQrIdentifier(utilisationCodeQrItem),
      );
      const utilisationCodeQrsToAdd = utilisationCodeQrs.filter(utilisationCodeQrItem => {
        const utilisationCodeQrIdentifier = this.getUtilisationCodeQrIdentifier(utilisationCodeQrItem);
        if (utilisationCodeQrCollectionIdentifiers.includes(utilisationCodeQrIdentifier)) {
          return false;
        }
        utilisationCodeQrCollectionIdentifiers.push(utilisationCodeQrIdentifier);
        return true;
      });
      return [...utilisationCodeQrsToAdd, ...utilisationCodeQrCollection];
    }
    return utilisationCodeQrCollection;
  }

  protected convertValueFromClient<T extends IUtilisationCodeQr | NewUtilisationCodeQr | PartialUpdateUtilisationCodeQr>(
    utilisationCodeQr: T,
  ): RestOf<T> {
    return {
      ...utilisationCodeQr,
      dateUtilisation: utilisationCodeQr.dateUtilisation?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestUtilisationCodeQr): IUtilisationCodeQr {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestUtilisationCodeQr[]): IUtilisationCodeQr[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
