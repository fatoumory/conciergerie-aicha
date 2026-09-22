import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ZoneService } from '../service/zone.service';
import { IZone } from '../zone.model';

const zoneResolve = (route: ActivatedRouteSnapshot): Observable<null | IZone> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ZoneService);
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

export default zoneResolve;
