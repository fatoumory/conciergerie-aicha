import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IQuotaService, NewQuotaService } from '../quota-service.model';

export type PartialUpdateQuotaService = Partial<IQuotaService> & Pick<IQuotaService, 'id'>;

type RestOf<T extends IQuotaService | NewQuotaService> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestQuotaService = RestOf<IQuotaService>;

export type NewRestQuotaService = RestOf<NewQuotaService>;

export type PartialUpdateRestQuotaService = RestOf<PartialUpdateQuotaService>;

@Service()
export class QuotaServicesService {
  readonly quotaServicesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly quotaServicesResource = httpResource<RestQuotaService[]>(() => {
    const params = this.quotaServicesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of quotaService that have been fetched. It is updated when the quotaServicesResource emits a new value.
   * In case of error while fetching the quotaServices, the signal is set to an empty array.
   */
  readonly quotaServices = computed(() =>
    (this.quotaServicesResource.hasValue() ? this.quotaServicesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/quota-services`;

  protected convertValueFromServer(restQuotaService: RestQuotaService): IQuotaService {
    return {
      ...restQuotaService,
      dateDebut: restQuotaService.dateDebut ? dayjs(restQuotaService.dateDebut) : undefined,
      dateFin: restQuotaService.dateFin ? dayjs(restQuotaService.dateFin) : undefined,
    };
  }
}

@Service()
export class QuotaServiceService extends QuotaServicesService {
  protected readonly http = inject(HttpClient);

  create(quotaService: NewQuotaService): Observable<IQuotaService> {
    const copy = this.convertValueFromClient(quotaService);
    return this.http.post<RestQuotaService>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(quotaService: IQuotaService): Observable<IQuotaService> {
    const copy = this.convertValueFromClient(quotaService);
    return this.http
      .put<RestQuotaService>(`${this.resourceUrl}/${encodeURIComponent(this.getQuotaServiceIdentifier(quotaService))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(quotaService: PartialUpdateQuotaService): Observable<IQuotaService> {
    const copy = this.convertValueFromClient(quotaService);
    return this.http
      .patch<RestQuotaService>(`${this.resourceUrl}/${encodeURIComponent(this.getQuotaServiceIdentifier(quotaService))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IQuotaService> {
    return this.http
      .get<RestQuotaService>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IQuotaService[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestQuotaService[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getQuotaServiceIdentifier(quotaService: Pick<IQuotaService, 'id'>): string {
    return quotaService.id;
  }

  compareQuotaService(o1: Pick<IQuotaService, 'id'> | null, o2: Pick<IQuotaService, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuotaServiceIdentifier(o1) === this.getQuotaServiceIdentifier(o2) : o1 === o2;
  }

  addQuotaServiceToCollectionIfMissing<Type extends Pick<IQuotaService, 'id'>>(
    quotaServiceCollection: Type[],
    ...quotaServicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quotaServices: Type[] = quotaServicesToCheck.filter(
      quotaServiceItem => quotaServiceItem !== null && quotaServiceItem !== undefined,
    );
    if (quotaServices.length > 0) {
      const quotaServiceCollectionIdentifiers = quotaServiceCollection.map(quotaServiceItem =>
        this.getQuotaServiceIdentifier(quotaServiceItem),
      );
      const quotaServicesToAdd = quotaServices.filter(quotaServiceItem => {
        const quotaServiceIdentifier = this.getQuotaServiceIdentifier(quotaServiceItem);
        if (quotaServiceCollectionIdentifiers.includes(quotaServiceIdentifier)) {
          return false;
        }
        quotaServiceCollectionIdentifiers.push(quotaServiceIdentifier);
        return true;
      });
      return [...quotaServicesToAdd, ...quotaServiceCollection];
    }
    return quotaServiceCollection;
  }

  protected convertValueFromClient<T extends IQuotaService | NewQuotaService | PartialUpdateQuotaService>(quotaService: T): RestOf<T> {
    return {
      ...quotaService,
      dateDebut: quotaService.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: quotaService.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestQuotaService): IQuotaService {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestQuotaService[]): IQuotaService[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
