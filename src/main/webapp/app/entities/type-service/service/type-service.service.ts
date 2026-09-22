import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITypeService, NewTypeService } from '../type-service.model';

export type PartialUpdateTypeService = Partial<ITypeService> & Pick<ITypeService, 'id'>;

@Service()
export class TypeServicesService {
  readonly typeServicesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly typeServicesResource = httpResource<ITypeService[]>(() => {
    const params = this.typeServicesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of typeService that have been fetched. It is updated when the typeServicesResource emits a new value.
   * In case of error while fetching the typeServices, the signal is set to an empty array.
   */
  readonly typeServices = computed(() => (this.typeServicesResource.hasValue() ? this.typeServicesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/type-services`;
}

@Service()
export class TypeServiceService extends TypeServicesService {
  protected readonly http = inject(HttpClient);

  create(typeService: NewTypeService): Observable<ITypeService> {
    return this.http.post<ITypeService>(this.resourceUrl, typeService);
  }

  update(typeService: ITypeService): Observable<ITypeService> {
    return this.http.put<ITypeService>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTypeServiceIdentifier(typeService))}`,
      typeService,
    );
  }

  partialUpdate(typeService: PartialUpdateTypeService): Observable<ITypeService> {
    return this.http.patch<ITypeService>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTypeServiceIdentifier(typeService))}`,
      typeService,
    );
  }

  find(id: string): Observable<ITypeService> {
    return this.http.get<ITypeService>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITypeService[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITypeService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTypeServiceIdentifier(typeService: Pick<ITypeService, 'id'>): string {
    return typeService.id;
  }

  compareTypeService(o1: Pick<ITypeService, 'id'> | null, o2: Pick<ITypeService, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeServiceIdentifier(o1) === this.getTypeServiceIdentifier(o2) : o1 === o2;
  }

  addTypeServiceToCollectionIfMissing<Type extends Pick<ITypeService, 'id'>>(
    typeServiceCollection: Type[],
    ...typeServicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeServices: Type[] = typeServicesToCheck.filter(typeServiceItem => typeServiceItem !== null && typeServiceItem !== undefined);
    if (typeServices.length > 0) {
      const typeServiceCollectionIdentifiers = typeServiceCollection.map(typeServiceItem => this.getTypeServiceIdentifier(typeServiceItem));
      const typeServicesToAdd = typeServices.filter(typeServiceItem => {
        const typeServiceIdentifier = this.getTypeServiceIdentifier(typeServiceItem);
        if (typeServiceCollectionIdentifiers.includes(typeServiceIdentifier)) {
          return false;
        }
        typeServiceCollectionIdentifiers.push(typeServiceIdentifier);
        return true;
      });
      return [...typeServicesToAdd, ...typeServiceCollection];
    }
    return typeServiceCollection;
  }
}
