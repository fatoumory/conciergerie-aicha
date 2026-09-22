import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICodeQrService } from '../code-qr-service.model';
import { CodeQrServiceService } from '../service/code-qr-service.service';

const codeQrServiceResolve = (route: ActivatedRouteSnapshot): Observable<null | ICodeQrService> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CodeQrServiceService);
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

export default codeQrServiceResolve;
