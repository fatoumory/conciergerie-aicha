import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IServiceConciergerie, NewServiceConciergerie } from '../service-conciergerie.model';

export type PartialUpdateServiceConciergerie = Partial<IServiceConciergerie> & Pick<IServiceConciergerie, 'id'>;

@Service()
export class ServiceConciergeriesService {
  readonly serviceConciergeriesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly serviceConciergeriesResource = httpResource<IServiceConciergerie[]>(() => {
    const params = this.serviceConciergeriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of serviceConciergerie that have been fetched. It is updated when the serviceConciergeriesResource emits a new value.
   * In case of error while fetching the serviceConciergeries, the signal is set to an empty array.
   */
  readonly serviceConciergeries = computed(() =>
    this.serviceConciergeriesResource.hasValue() ? this.serviceConciergeriesResource.value() : [],
  );
  protected readonly resourceUrl = `${serverApiUrl}api/service-conciergeries`;
}

@Service()
export class ServiceConciergerieService extends ServiceConciergeriesService {
  protected readonly http = inject(HttpClient);

  create(serviceConciergerie: NewServiceConciergerie): Observable<IServiceConciergerie> {
    return this.http.post<IServiceConciergerie>(this.resourceUrl, serviceConciergerie);
  }

  update(serviceConciergerie: IServiceConciergerie): Observable<IServiceConciergerie> {
    return this.http.put<IServiceConciergerie>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServiceConciergerieIdentifier(serviceConciergerie))}`,
      serviceConciergerie,
    );
  }

  partialUpdate(serviceConciergerie: PartialUpdateServiceConciergerie): Observable<IServiceConciergerie> {
    return this.http.patch<IServiceConciergerie>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServiceConciergerieIdentifier(serviceConciergerie))}`,
      serviceConciergerie,
    );
  }

  find(id: string): Observable<IServiceConciergerie> {
    return this.http.get<IServiceConciergerie>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IServiceConciergerie[]>> {
    const options = createRequestOption(req);
    return this.http.get<IServiceConciergerie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getServiceConciergerieIdentifier(serviceConciergerie: Pick<IServiceConciergerie, 'id'>): string {
    return serviceConciergerie.id;
  }

  compareServiceConciergerie(o1: Pick<IServiceConciergerie, 'id'> | null, o2: Pick<IServiceConciergerie, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceConciergerieIdentifier(o1) === this.getServiceConciergerieIdentifier(o2) : o1 === o2;
  }

  addServiceConciergerieToCollectionIfMissing<Type extends Pick<IServiceConciergerie, 'id'>>(
    serviceConciergerieCollection: Type[],
    ...serviceConciergeriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceConciergeries: Type[] = serviceConciergeriesToCheck.filter(
      serviceConciergerieItem => serviceConciergerieItem !== null && serviceConciergerieItem !== undefined,
    );
    if (serviceConciergeries.length > 0) {
      const serviceConciergerieCollectionIdentifiers = serviceConciergerieCollection.map(serviceConciergerieItem =>
        this.getServiceConciergerieIdentifier(serviceConciergerieItem),
      );
      const serviceConciergeriesToAdd = serviceConciergeries.filter(serviceConciergerieItem => {
        const serviceConciergerieIdentifier = this.getServiceConciergerieIdentifier(serviceConciergerieItem);
        if (serviceConciergerieCollectionIdentifiers.includes(serviceConciergerieIdentifier)) {
          return false;
        }
        serviceConciergerieCollectionIdentifiers.push(serviceConciergerieIdentifier);
        return true;
      });
      return [...serviceConciergeriesToAdd, ...serviceConciergerieCollection];
    }
    return serviceConciergerieCollection;
  }
}
