import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICouverturePartenaire } from '../couverture-partenaire.model';
import { CouverturePartenaireService } from '../service/couverture-partenaire.service';

const couverturePartenaireResolve = (route: ActivatedRouteSnapshot): Observable<null | ICouverturePartenaire> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CouverturePartenaireService);
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

export default couverturePartenaireResolve;
