import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPartenaireZone, NewPartenaireZone } from '../partenaire-zone.model';

export type PartialUpdatePartenaireZone = Partial<IPartenaireZone> & Pick<IPartenaireZone, 'id'>;

@Service()
export class PartenaireZonesService {
  readonly partenaireZonesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly partenaireZonesResource = httpResource<IPartenaireZone[]>(() => {
    const params = this.partenaireZonesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of partenaireZone that have been fetched. It is updated when the partenaireZonesResource emits a new value.
   * In case of error while fetching the partenaireZones, the signal is set to an empty array.
   */
  readonly partenaireZones = computed(() => (this.partenaireZonesResource.hasValue() ? this.partenaireZonesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/partenaire-zones`;
}

@Service()
export class PartenaireZoneService extends PartenaireZonesService {
  protected readonly http = inject(HttpClient);

  create(partenaireZone: NewPartenaireZone): Observable<IPartenaireZone> {
    return this.http.post<IPartenaireZone>(this.resourceUrl, partenaireZone);
  }

  update(partenaireZone: IPartenaireZone): Observable<IPartenaireZone> {
    return this.http.put<IPartenaireZone>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartenaireZoneIdentifier(partenaireZone))}`,
      partenaireZone,
    );
  }

  partialUpdate(partenaireZone: PartialUpdatePartenaireZone): Observable<IPartenaireZone> {
    return this.http.patch<IPartenaireZone>(
      `${this.resourceUrl}/${encodeURIComponent(this.getPartenaireZoneIdentifier(partenaireZone))}`,
      partenaireZone,
    );
  }

  find(id: string): Observable<IPartenaireZone> {
    return this.http.get<IPartenaireZone>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPartenaireZone[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPartenaireZone[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPartenaireZoneIdentifier(partenaireZone: Pick<IPartenaireZone, 'id'>): string {
    return partenaireZone.id;
  }

  comparePartenaireZone(o1: Pick<IPartenaireZone, 'id'> | null, o2: Pick<IPartenaireZone, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartenaireZoneIdentifier(o1) === this.getPartenaireZoneIdentifier(o2) : o1 === o2;
  }

  addPartenaireZoneToCollectionIfMissing<Type extends Pick<IPartenaireZone, 'id'>>(
    partenaireZoneCollection: Type[],
    ...partenaireZonesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const partenaireZones: Type[] = partenaireZonesToCheck.filter(
      partenaireZoneItem => partenaireZoneItem !== null && partenaireZoneItem !== undefined,
    );
    if (partenaireZones.length > 0) {
      const partenaireZoneCollectionIdentifiers = partenaireZoneCollection.map(partenaireZoneItem =>
        this.getPartenaireZoneIdentifier(partenaireZoneItem),
      );
      const partenaireZonesToAdd = partenaireZones.filter(partenaireZoneItem => {
        const partenaireZoneIdentifier = this.getPartenaireZoneIdentifier(partenaireZoneItem);
        if (partenaireZoneCollectionIdentifiers.includes(partenaireZoneIdentifier)) {
          return false;
        }
        partenaireZoneCollectionIdentifiers.push(partenaireZoneIdentifier);
        return true;
      });
      return [...partenaireZonesToAdd, ...partenaireZoneCollection];
    }
    return partenaireZoneCollection;
  }
}
