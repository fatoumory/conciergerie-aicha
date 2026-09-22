import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IEligibiliteService } from '../eligibilite-service.model';
import { EligibiliteServiceService } from '../service/eligibilite-service.service';

const eligibiliteServiceResolve = (route: ActivatedRouteSnapshot): Observable<null | IEligibiliteService> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(EligibiliteServiceService);
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

export default eligibiliteServiceResolve;
