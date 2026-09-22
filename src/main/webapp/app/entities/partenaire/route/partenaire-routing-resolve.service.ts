import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartenaire } from '../partenaire.model';
import { PartenaireService } from '../service/partenaire.service';

const partenaireResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartenaire> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartenaireService);
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

export default partenaireResolve;
