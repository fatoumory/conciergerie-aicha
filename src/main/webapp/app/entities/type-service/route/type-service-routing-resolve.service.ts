import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TypeServiceService } from '../service/type-service.service';
import { ITypeService } from '../type-service.model';

const typeServiceResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeService> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TypeServiceService);
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

export default typeServiceResolve;
