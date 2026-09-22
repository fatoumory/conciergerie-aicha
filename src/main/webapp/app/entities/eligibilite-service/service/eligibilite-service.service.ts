import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IEligibiliteService, NewEligibiliteService } from '../eligibilite-service.model';

export type PartialUpdateEligibiliteService = Partial<IEligibiliteService> & Pick<IEligibiliteService, 'id'>;

type RestOf<T extends IEligibiliteService | NewEligibiliteService> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestEligibiliteService = RestOf<IEligibiliteService>;

export type NewRestEligibiliteService = RestOf<NewEligibiliteService>;

export type PartialUpdateRestEligibiliteService = RestOf<PartialUpdateEligibiliteService>;

@Service()
export class EligibiliteServicesService {
  readonly eligibiliteServicesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly eligibiliteServicesResource = httpResource<RestEligibiliteService[]>(() => {
    const params = this.eligibiliteServicesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of eligibiliteService that have been fetched. It is updated when the eligibiliteServicesResource emits a new value.
   * In case of error while fetching the eligibiliteServices, the signal is set to an empty array.
   */
  readonly eligibiliteServices = computed(() =>
    (this.eligibiliteServicesResource.hasValue() ? this.eligibiliteServicesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/eligibilite-services`;

  protected convertValueFromServer(restEligibiliteService: RestEligibiliteService): IEligibiliteService {
    return {
      ...restEligibiliteService,
      dateDebut: restEligibiliteService.dateDebut ? dayjs(restEligibiliteService.dateDebut) : undefined,
      dateFin: restEligibiliteService.dateFin ? dayjs(restEligibiliteService.dateFin) : undefined,
    };
  }
}

@Service()
export class EligibiliteServiceService extends EligibiliteServicesService {
  protected readonly http = inject(HttpClient);

  create(eligibiliteService: NewEligibiliteService): Observable<IEligibiliteService> {
    const copy = this.convertValueFromClient(eligibiliteService);
    return this.http.post<RestEligibiliteService>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(eligibiliteService: IEligibiliteService): Observable<IEligibiliteService> {
    const copy = this.convertValueFromClient(eligibiliteService);
    return this.http
      .put<RestEligibiliteService>(
        `${this.resourceUrl}/${encodeURIComponent(this.getEligibiliteServiceIdentifier(eligibiliteService))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(eligibiliteService: PartialUpdateEligibiliteService): Observable<IEligibiliteService> {
    const copy = this.convertValueFromClient(eligibiliteService);
    return this.http
      .patch<RestEligibiliteService>(
        `${this.resourceUrl}/${encodeURIComponent(this.getEligibiliteServiceIdentifier(eligibiliteService))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IEligibiliteService> {
    return this.http
      .get<RestEligibiliteService>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IEligibiliteService[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEligibiliteService[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEligibiliteServiceIdentifier(eligibiliteService: Pick<IEligibiliteService, 'id'>): string {
    return eligibiliteService.id;
  }

  compareEligibiliteService(o1: Pick<IEligibiliteService, 'id'> | null, o2: Pick<IEligibiliteService, 'id'> | null): boolean {
    return o1 && o2 ? this.getEligibiliteServiceIdentifier(o1) === this.getEligibiliteServiceIdentifier(o2) : o1 === o2;
  }

  addEligibiliteServiceToCollectionIfMissing<Type extends Pick<IEligibiliteService, 'id'>>(
    eligibiliteServiceCollection: Type[],
    ...eligibiliteServicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eligibiliteServices: Type[] = eligibiliteServicesToCheck.filter(
      eligibiliteServiceItem => eligibiliteServiceItem !== null && eligibiliteServiceItem !== undefined,
    );
    if (eligibiliteServices.length > 0) {
      const eligibiliteServiceCollectionIdentifiers = eligibiliteServiceCollection.map(eligibiliteServiceItem =>
        this.getEligibiliteServiceIdentifier(eligibiliteServiceItem),
      );
      const eligibiliteServicesToAdd = eligibiliteServices.filter(eligibiliteServiceItem => {
        const eligibiliteServiceIdentifier = this.getEligibiliteServiceIdentifier(eligibiliteServiceItem);
        if (eligibiliteServiceCollectionIdentifiers.includes(eligibiliteServiceIdentifier)) {
          return false;
        }
        eligibiliteServiceCollectionIdentifiers.push(eligibiliteServiceIdentifier);
        return true;
      });
      return [...eligibiliteServicesToAdd, ...eligibiliteServiceCollection];
    }
    return eligibiliteServiceCollection;
  }

  protected convertValueFromClient<T extends IEligibiliteService | NewEligibiliteService | PartialUpdateEligibiliteService>(
    eligibiliteService: T,
  ): RestOf<T> {
    return {
      ...eligibiliteService,
      dateDebut: eligibiliteService.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: eligibiliteService.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestEligibiliteService): IEligibiliteService {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestEligibiliteService[]): IEligibiliteService[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
