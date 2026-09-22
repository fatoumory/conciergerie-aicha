import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IAffectationDemande } from '../affectation-demande.model';
import { AffectationDemandeService } from '../service/affectation-demande.service';

const affectationDemandeResolve = (route: ActivatedRouteSnapshot): Observable<null | IAffectationDemande> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(AffectationDemandeService);
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

export default affectationDemandeResolve;
