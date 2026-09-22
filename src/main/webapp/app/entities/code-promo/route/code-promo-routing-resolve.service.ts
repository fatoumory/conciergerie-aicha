import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICodePromo } from '../code-promo.model';
import { CodePromoService } from '../service/code-promo.service';

const codePromoResolve = (route: ActivatedRouteSnapshot): Observable<null | ICodePromo> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CodePromoService);
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

export default codePromoResolve;
