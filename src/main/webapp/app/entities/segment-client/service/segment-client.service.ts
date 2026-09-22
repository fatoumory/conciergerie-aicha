import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ISegmentClient, NewSegmentClient } from '../segment-client.model';

export type PartialUpdateSegmentClient = Partial<ISegmentClient> & Pick<ISegmentClient, 'id'>;

@Service()
export class SegmentClientsService {
  readonly segmentClientsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly segmentClientsResource = httpResource<ISegmentClient[]>(() => {
    const params = this.segmentClientsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of segmentClient that have been fetched. It is updated when the segmentClientsResource emits a new value.
   * In case of error while fetching the segmentClients, the signal is set to an empty array.
   */
  readonly segmentClients = computed(() => (this.segmentClientsResource.hasValue() ? this.segmentClientsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/segment-clients`;
}

@Service()
export class SegmentClientService extends SegmentClientsService {
  protected readonly http = inject(HttpClient);

  create(segmentClient: NewSegmentClient): Observable<ISegmentClient> {
    return this.http.post<ISegmentClient>(this.resourceUrl, segmentClient);
  }

  update(segmentClient: ISegmentClient): Observable<ISegmentClient> {
    return this.http.put<ISegmentClient>(
      `${this.resourceUrl}/${encodeURIComponent(this.getSegmentClientIdentifier(segmentClient))}`,
      segmentClient,
    );
  }

  partialUpdate(segmentClient: PartialUpdateSegmentClient): Observable<ISegmentClient> {
    return this.http.patch<ISegmentClient>(
      `${this.resourceUrl}/${encodeURIComponent(this.getSegmentClientIdentifier(segmentClient))}`,
      segmentClient,
    );
  }

  find(id: string): Observable<ISegmentClient> {
    return this.http.get<ISegmentClient>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ISegmentClient[]>> {
    const options = createRequestOption(req);
    return this.http.get<ISegmentClient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getSegmentClientIdentifier(segmentClient: Pick<ISegmentClient, 'id'>): string {
    return segmentClient.id;
  }

  compareSegmentClient(o1: Pick<ISegmentClient, 'id'> | null, o2: Pick<ISegmentClient, 'id'> | null): boolean {
    return o1 && o2 ? this.getSegmentClientIdentifier(o1) === this.getSegmentClientIdentifier(o2) : o1 === o2;
  }

  addSegmentClientToCollectionIfMissing<Type extends Pick<ISegmentClient, 'id'>>(
    segmentClientCollection: Type[],
    ...segmentClientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const segmentClients: Type[] = segmentClientsToCheck.filter(
      segmentClientItem => segmentClientItem !== null && segmentClientItem !== undefined,
    );
    if (segmentClients.length > 0) {
      const segmentClientCollectionIdentifiers = segmentClientCollection.map(segmentClientItem =>
        this.getSegmentClientIdentifier(segmentClientItem),
      );
      const segmentClientsToAdd = segmentClients.filter(segmentClientItem => {
        const segmentClientIdentifier = this.getSegmentClientIdentifier(segmentClientItem);
        if (segmentClientCollectionIdentifiers.includes(segmentClientIdentifier)) {
          return false;
        }
        segmentClientCollectionIdentifiers.push(segmentClientIdentifier);
        return true;
      });
      return [...segmentClientsToAdd, ...segmentClientCollection];
    }
    return segmentClientCollection;
  }
}
