import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { StatutDemandeService } from '../service/statut-demande.service';
import { IStatutDemande } from '../statut-demande.model';

const statutDemandeResolve = (route: ActivatedRouteSnapshot): Observable<null | IStatutDemande> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(StatutDemandeService);
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

export default statutDemandeResolve;
