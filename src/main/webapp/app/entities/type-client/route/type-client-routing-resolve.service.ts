import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TypeClientService } from '../service/type-client.service';
import { ITypeClient } from '../type-client.model';

const typeClientResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeClient> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TypeClientService);
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

export default typeClientResolve;
