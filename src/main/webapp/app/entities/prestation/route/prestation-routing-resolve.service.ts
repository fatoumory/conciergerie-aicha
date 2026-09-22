import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPrestation } from '../prestation.model';
import { PrestationService } from '../service/prestation.service';

const prestationResolve = (route: ActivatedRouteSnapshot): Observable<null | IPrestation> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PrestationService);
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

export default prestationResolve;
