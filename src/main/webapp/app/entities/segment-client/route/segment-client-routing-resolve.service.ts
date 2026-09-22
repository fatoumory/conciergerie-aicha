import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ISegmentClient } from '../segment-client.model';
import { SegmentClientService } from '../service/segment-client.service';

const segmentClientResolve = (route: ActivatedRouteSnapshot): Observable<null | ISegmentClient> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(SegmentClientService);
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

export default segmentClientResolve;
