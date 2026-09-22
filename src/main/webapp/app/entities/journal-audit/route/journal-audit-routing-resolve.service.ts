import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IJournalAudit } from '../journal-audit.model';
import { JournalAuditService } from '../service/journal-audit.service';

const journalAuditResolve = (route: ActivatedRouteSnapshot): Observable<null | IJournalAudit> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(JournalAuditService);
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

export default journalAuditResolve;
