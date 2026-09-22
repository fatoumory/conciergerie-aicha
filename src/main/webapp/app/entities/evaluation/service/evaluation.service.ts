import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IEvaluation, NewEvaluation } from '../evaluation.model';

export type PartialUpdateEvaluation = Partial<IEvaluation> & Pick<IEvaluation, 'id'>;

type RestOf<T extends IEvaluation | NewEvaluation> = Omit<T, 'dateEvaluation'> & {
  dateEvaluation?: string | null;
};

export type RestEvaluation = RestOf<IEvaluation>;

export type NewRestEvaluation = RestOf<NewEvaluation>;

export type PartialUpdateRestEvaluation = RestOf<PartialUpdateEvaluation>;

@Service()
export class EvaluationsService {
  readonly evaluationsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly evaluationsResource = httpResource<RestEvaluation[]>(() => {
    const params = this.evaluationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of evaluation that have been fetched. It is updated when the evaluationsResource emits a new value.
   * In case of error while fetching the evaluations, the signal is set to an empty array.
   */
  readonly evaluations = computed(() =>
    (this.evaluationsResource.hasValue() ? this.evaluationsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/evaluations`;

  protected convertValueFromServer(restEvaluation: RestEvaluation): IEvaluation {
    return {
      ...restEvaluation,
      dateEvaluation: restEvaluation.dateEvaluation ? dayjs(restEvaluation.dateEvaluation) : undefined,
    };
  }
}

@Service()
export class EvaluationService extends EvaluationsService {
  protected readonly http = inject(HttpClient);

  create(evaluation: NewEvaluation): Observable<IEvaluation> {
    const copy = this.convertValueFromClient(evaluation);
    return this.http.post<RestEvaluation>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(evaluation: IEvaluation): Observable<IEvaluation> {
    const copy = this.convertValueFromClient(evaluation);
    return this.http
      .put<RestEvaluation>(`${this.resourceUrl}/${encodeURIComponent(this.getEvaluationIdentifier(evaluation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(evaluation: PartialUpdateEvaluation): Observable<IEvaluation> {
    const copy = this.convertValueFromClient(evaluation);
    return this.http
      .patch<RestEvaluation>(`${this.resourceUrl}/${encodeURIComponent(this.getEvaluationIdentifier(evaluation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<IEvaluation> {
    return this.http
      .get<RestEvaluation>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IEvaluation[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEvaluation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEvaluationIdentifier(evaluation: Pick<IEvaluation, 'id'>): string {
    return evaluation.id;
  }

  compareEvaluation(o1: Pick<IEvaluation, 'id'> | null, o2: Pick<IEvaluation, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvaluationIdentifier(o1) === this.getEvaluationIdentifier(o2) : o1 === o2;
  }

  addEvaluationToCollectionIfMissing<Type extends Pick<IEvaluation, 'id'>>(
    evaluationCollection: Type[],
    ...evaluationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evaluations: Type[] = evaluationsToCheck.filter(evaluationItem => evaluationItem !== null && evaluationItem !== undefined);
    if (evaluations.length > 0) {
      const evaluationCollectionIdentifiers = evaluationCollection.map(evaluationItem => this.getEvaluationIdentifier(evaluationItem));
      const evaluationsToAdd = evaluations.filter(evaluationItem => {
        const evaluationIdentifier = this.getEvaluationIdentifier(evaluationItem);
        if (evaluationCollectionIdentifiers.includes(evaluationIdentifier)) {
          return false;
        }
        evaluationCollectionIdentifiers.push(evaluationIdentifier);
        return true;
      });
      return [...evaluationsToAdd, ...evaluationCollection];
    }
    return evaluationCollection;
  }

  protected convertValueFromClient<T extends IEvaluation | NewEvaluation | PartialUpdateEvaluation>(evaluation: T): RestOf<T> {
    return {
      ...evaluation,
      dateEvaluation: evaluation.dateEvaluation?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestEvaluation): IEvaluation {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestEvaluation[]): IEvaluation[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
