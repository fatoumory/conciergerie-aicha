import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IHistoriqueStatutDemande } from '../historique-statut-demande.model';
import { HistoriqueStatutDemandeService } from '../service/historique-statut-demande.service';

const historiqueStatutDemandeResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistoriqueStatutDemande> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(HistoriqueStatutDemandeService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default historiqueStatutDemandeResolve;
