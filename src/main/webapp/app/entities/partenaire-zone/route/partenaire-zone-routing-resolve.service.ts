import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPartenaireZone } from '../partenaire-zone.model';
import { PartenaireZoneService } from '../service/partenaire-zone.service';

const partenaireZoneResolve = (route: ActivatedRouteSnapshot): Observable<null | IPartenaireZone> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PartenaireZoneService);
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

export default partenaireZoneResolve;
