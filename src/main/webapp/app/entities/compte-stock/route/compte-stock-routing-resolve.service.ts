import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICompteStock } from '../compte-stock.model';
import { CompteStockService } from '../service/compte-stock.service';

const compteStockResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompteStock> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CompteStockService);
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

export default compteStockResolve;
