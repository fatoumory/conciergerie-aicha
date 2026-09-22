import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IQuotaService } from '../quota-service.model';
import { QuotaServiceService } from '../service/quota-service.service';

const quotaServiceResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuotaService> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(QuotaServiceService);
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

export default quotaServiceResolve;
