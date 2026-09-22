import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IStatutDemande, NewStatutDemande } from '../statut-demande.model';

export type PartialUpdateStatutDemande = Partial<IStatutDemande> & Pick<IStatutDemande, 'id'>;

@Service()
export class StatutDemandesService {
  readonly statutDemandesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly statutDemandesResource = httpResource<IStatutDemande[]>(() => {
    const params = this.statutDemandesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of statutDemande that have been fetched. It is updated when the statutDemandesResource emits a new value.
   * In case of error while fetching the statutDemandes, the signal is set to an empty array.
   */
  readonly statutDemandes = computed(() => (this.statutDemandesResource.hasValue() ? this.statutDemandesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/statut-demandes`;
}

@Service()
export class StatutDemandeService extends StatutDemandesService {
  protected readonly http = inject(HttpClient);

  create(statutDemande: NewStatutDemande): Observable<IStatutDemande> {
    return this.http.post<IStatutDemande>(this.resourceUrl, statutDemande);
  }

  update(statutDemande: IStatutDemande): Observable<IStatutDemande> {
    return this.http.put<IStatutDemande>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStatutDemandeIdentifier(statutDemande))}`,
      statutDemande,
    );
  }

  partialUpdate(statutDemande: PartialUpdateStatutDemande): Observable<IStatutDemande> {
    return this.http.patch<IStatutDemande>(
      `${this.resourceUrl}/${encodeURIComponent(this.getStatutDemandeIdentifier(statutDemande))}`,
      statutDemande,
    );
  }

  find(id: string): Observable<IStatutDemande> {
    return this.http.get<IStatutDemande>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IStatutDemande[]>> {
    const options = createRequestOption(req);
    return this.http.get<IStatutDemande[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getStatutDemandeIdentifier(statutDemande: Pick<IStatutDemande, 'id'>): string {
    return statutDemande.id;
  }

  compareStatutDemande(o1: Pick<IStatutDemande, 'id'> | null, o2: Pick<IStatutDemande, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatutDemandeIdentifier(o1) === this.getStatutDemandeIdentifier(o2) : o1 === o2;
  }

  addStatutDemandeToCollectionIfMissing<Type extends Pick<IStatutDemande, 'id'>>(
    statutDemandeCollection: Type[],
    ...statutDemandesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statutDemandes: Type[] = statutDemandesToCheck.filter(
      statutDemandeItem => statutDemandeItem !== null && statutDemandeItem !== undefined,
    );
    if (statutDemandes.length > 0) {
      const statutDemandeCollectionIdentifiers = statutDemandeCollection.map(statutDemandeItem =>
        this.getStatutDemandeIdentifier(statutDemandeItem),
      );
      const statutDemandesToAdd = statutDemandes.filter(statutDemandeItem => {
        const statutDemandeIdentifier = this.getStatutDemandeIdentifier(statutDemandeItem);
        if (statutDemandeCollectionIdentifiers.includes(statutDemandeIdentifier)) {
          return false;
        }
        statutDemandeCollectionIdentifiers.push(statutDemandeIdentifier);
        return true;
      });
      return [...statutDemandesToAdd, ...statutDemandeCollection];
    }
    return statutDemandeCollection;
  }
}
