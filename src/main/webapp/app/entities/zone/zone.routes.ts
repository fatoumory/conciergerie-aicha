import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import ZoneResolve from './route/zone-routing-resolve.service';

const zoneRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/zone').then(m => m.Zone),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/zone-detail').then(m => m.ZoneDetail),
    resolve: {
      zone: ZoneResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/zone-update').then(m => m.ZoneUpdate),
    resolve: {
      zone: ZoneResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/zone-update').then(m => m.ZoneUpdate),
    resolve: {
      zone: ZoneResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default zoneRoute;
