import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TransactionPaiementService } from '../service/transaction-paiement.service';
import { ITransactionPaiement } from '../transaction-paiement.model';

const transactionPaiementResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransactionPaiement> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TransactionPaiementService);
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

export default transactionPaiementResolve;
