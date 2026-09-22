import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IQuotaDetail, NewQuotaDetail } from '../quota-detail.model';

export type PartialUpdateQuotaDetail = Partial<IQuotaDetail> & Pick<IQuotaDetail, 'id'>;

@Service()
export class QuotaDetailsService {
  readonly quotaDetailsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly quotaDetailsResource = httpResource<IQuotaDetail[]>(() => {
    const params = this.quotaDetailsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of quotaDetail that have been fetched. It is updated when the quotaDetailsResource emits a new value.
   * In case of error while fetching the quotaDetails, the signal is set to an empty array.
   */
  readonly quotaDetails = computed(() => (this.quotaDetailsResource.hasValue() ? this.quotaDetailsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/quota-details`;
}

@Service()
export class QuotaDetailService extends QuotaDetailsService {
  protected readonly http = inject(HttpClient);

  create(quotaDetail: NewQuotaDetail): Observable<IQuotaDetail> {
    return this.http.post<IQuotaDetail>(this.resourceUrl, quotaDetail);
  }

  update(quotaDetail: IQuotaDetail): Observable<IQuotaDetail> {
    return this.http.put<IQuotaDetail>(
      `${this.resourceUrl}/${encodeURIComponent(this.getQuotaDetailIdentifier(quotaDetail))}`,
      quotaDetail,
    );
  }

  partialUpdate(quotaDetail: PartialUpdateQuotaDetail): Observable<IQuotaDetail> {
    return this.http.patch<IQuotaDetail>(
      `${this.resourceUrl}/${encodeURIComponent(this.getQuotaDetailIdentifier(quotaDetail))}`,
      quotaDetail,
    );
  }

  find(id: string): Observable<IQuotaDetail> {
    return this.http.get<IQuotaDetail>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IQuotaDetail[]>> {
    const options = createRequestOption(req);
    return this.http.get<IQuotaDetail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getQuotaDetailIdentifier(quotaDetail: Pick<IQuotaDetail, 'id'>): string {
    return quotaDetail.id;
  }

  compareQuotaDetail(o1: Pick<IQuotaDetail, 'id'> | null, o2: Pick<IQuotaDetail, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuotaDetailIdentifier(o1) === this.getQuotaDetailIdentifier(o2) : o1 === o2;
  }

  addQuotaDetailToCollectionIfMissing<Type extends Pick<IQuotaDetail, 'id'>>(
    quotaDetailCollection: Type[],
    ...quotaDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quotaDetails: Type[] = quotaDetailsToCheck.filter(quotaDetailItem => quotaDetailItem !== null && quotaDetailItem !== undefined);
    if (quotaDetails.length > 0) {
      const quotaDetailCollectionIdentifiers = quotaDetailCollection.map(quotaDetailItem => this.getQuotaDetailIdentifier(quotaDetailItem));
      const quotaDetailsToAdd = quotaDetails.filter(quotaDetailItem => {
        const quotaDetailIdentifier = this.getQuotaDetailIdentifier(quotaDetailItem);
        if (quotaDetailCollectionIdentifiers.includes(quotaDetailIdentifier)) {
          return false;
        }
        quotaDetailCollectionIdentifiers.push(quotaDetailIdentifier);
        return true;
      });
      return [...quotaDetailsToAdd, ...quotaDetailCollection];
    }
    return quotaDetailCollection;
  }
}
