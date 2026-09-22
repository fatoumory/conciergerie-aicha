import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IZone, NewZone } from '../zone.model';

export type PartialUpdateZone = Partial<IZone> & Pick<IZone, 'id'>;

@Service()
export class ZonesService {
  readonly zonesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly zonesResource = httpResource<IZone[]>(() => {
    const params = this.zonesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of zone that have been fetched. It is updated when the zonesResource emits a new value.
   * In case of error while fetching the zones, the signal is set to an empty array.
   */
  readonly zones = computed(() => (this.zonesResource.hasValue() ? this.zonesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/zones`;
}

@Service()
export class ZoneService extends ZonesService {
  protected readonly http = inject(HttpClient);

  create(zone: NewZone): Observable<IZone> {
    return this.http.post<IZone>(this.resourceUrl, zone);
  }

  update(zone: IZone): Observable<IZone> {
    return this.http.put<IZone>(`${this.resourceUrl}/${encodeURIComponent(this.getZoneIdentifier(zone))}`, zone);
  }

  partialUpdate(zone: PartialUpdateZone): Observable<IZone> {
    return this.http.patch<IZone>(`${this.resourceUrl}/${encodeURIComponent(this.getZoneIdentifier(zone))}`, zone);
  }

  find(id: string): Observable<IZone> {
    return this.http.get<IZone>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IZone[]>> {
    const options = createRequestOption(req);
    return this.http.get<IZone[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getZoneIdentifier(zone: Pick<IZone, 'id'>): string {
    return zone.id;
  }

  compareZone(o1: Pick<IZone, 'id'> | null, o2: Pick<IZone, 'id'> | null): boolean {
    return o1 && o2 ? this.getZoneIdentifier(o1) === this.getZoneIdentifier(o2) : o1 === o2;
  }

  addZoneToCollectionIfMissing<Type extends Pick<IZone, 'id'>>(
    zoneCollection: Type[],
    ...zonesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const zones: Type[] = zonesToCheck.filter(zoneItem => zoneItem !== null && zoneItem !== undefined);
    if (zones.length > 0) {
      const zoneCollectionIdentifiers = zoneCollection.map(zoneItem => this.getZoneIdentifier(zoneItem));
      const zonesToAdd = zones.filter(zoneItem => {
        const zoneIdentifier = this.getZoneIdentifier(zoneItem);
        if (zoneCollectionIdentifiers.includes(zoneIdentifier)) {
          return false;
        }
        zoneCollectionIdentifiers.push(zoneIdentifier);
        return true;
      });
      return [...zonesToAdd, ...zoneCollection];
    }
    return zoneCollection;
  }
}
