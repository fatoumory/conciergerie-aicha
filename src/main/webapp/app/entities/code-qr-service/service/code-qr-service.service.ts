import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICodeQrService, NewCodeQrService } from '../code-qr-service.model';

export type PartialUpdateCodeQrService = Partial<ICodeQrService> & Pick<ICodeQrService, 'id'>;

type RestOf<T extends ICodeQrService | NewCodeQrService> = Omit<T, 'dateGeneration' | 'dateExpiration'> & {
  dateGeneration?: string | null;
  dateExpiration?: string | null;
};

export type RestCodeQrService = RestOf<ICodeQrService>;

export type NewRestCodeQrService = RestOf<NewCodeQrService>;

export type PartialUpdateRestCodeQrService = RestOf<PartialUpdateCodeQrService>;

@Service()
export class CodeQrServicesService {
  readonly codeQrServicesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly codeQrServicesResource = httpResource<RestCodeQrService[]>(() => {
    const params = this.codeQrServicesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of codeQrService that have been fetched. It is updated when the codeQrServicesResource emits a new value.
   * In case of error while fetching the codeQrServices, the signal is set to an empty array.
   */
  readonly codeQrServices = computed(() =>
    (this.codeQrServicesResource.hasValue() ? this.codeQrServicesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/code-qr-services`;

  protected convertValueFromServer(restCodeQrService: RestCodeQrService): ICodeQrService {
    return {
      ...restCodeQrService,
      dateGeneration: restCodeQrService.dateGeneration ? dayjs(restCodeQrService.dateGeneration) : undefined,
      dateExpiration: restCodeQrService.dateExpiration ? dayjs(restCodeQrService.dateExpiration) : undefined,
    };
  }
}

@Service()
export class CodeQrServiceService extends CodeQrServicesService {
  protected readonly http = inject(HttpClient);

  create(codeQrService: NewCodeQrService): Observable<ICodeQrService> {
    const copy = this.convertValueFromClient(codeQrService);
    return this.http.post<RestCodeQrService>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(codeQrService: ICodeQrService): Observable<ICodeQrService> {
    const copy = this.convertValueFromClient(codeQrService);
    return this.http
      .put<RestCodeQrService>(`${this.resourceUrl}/${encodeURIComponent(this.getCodeQrServiceIdentifier(codeQrService))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(codeQrService: PartialUpdateCodeQrService): Observable<ICodeQrService> {
    const copy = this.convertValueFromClient(codeQrService);
    return this.http
      .patch<RestCodeQrService>(`${this.resourceUrl}/${encodeURIComponent(this.getCodeQrServiceIdentifier(codeQrService))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<ICodeQrService> {
    return this.http
      .get<RestCodeQrService>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICodeQrService[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCodeQrService[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCodeQrServiceIdentifier(codeQrService: Pick<ICodeQrService, 'id'>): string {
    return codeQrService.id;
  }

  compareCodeQrService(o1: Pick<ICodeQrService, 'id'> | null, o2: Pick<ICodeQrService, 'id'> | null): boolean {
    return o1 && o2 ? this.getCodeQrServiceIdentifier(o1) === this.getCodeQrServiceIdentifier(o2) : o1 === o2;
  }

  addCodeQrServiceToCollectionIfMissing<Type extends Pick<ICodeQrService, 'id'>>(
    codeQrServiceCollection: Type[],
    ...codeQrServicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const codeQrServices: Type[] = codeQrServicesToCheck.filter(
      codeQrServiceItem => codeQrServiceItem !== null && codeQrServiceItem !== undefined,
    );
    if (codeQrServices.length > 0) {
      const codeQrServiceCollectionIdentifiers = codeQrServiceCollection.map(codeQrServiceItem =>
        this.getCodeQrServiceIdentifier(codeQrServiceItem),
      );
      const codeQrServicesToAdd = codeQrServices.filter(codeQrServiceItem => {
        const codeQrServiceIdentifier = this.getCodeQrServiceIdentifier(codeQrServiceItem);
        if (codeQrServiceCollectionIdentifiers.includes(codeQrServiceIdentifier)) {
          return false;
        }
        codeQrServiceCollectionIdentifiers.push(codeQrServiceIdentifier);
        return true;
      });
      return [...codeQrServicesToAdd, ...codeQrServiceCollection];
    }
    return codeQrServiceCollection;
  }

  protected convertValueFromClient<T extends ICodeQrService | NewCodeQrService | PartialUpdateCodeQrService>(codeQrService: T): RestOf<T> {
    return {
      ...codeQrService,
      dateGeneration: codeQrService.dateGeneration?.toJSON() ?? null,
      dateExpiration: codeQrService.dateExpiration?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCodeQrService): ICodeQrService {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCodeQrService[]): ICodeQrService[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
