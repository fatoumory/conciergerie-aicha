import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import PartenaireZoneResolve from './route/partenaire-zone-routing-resolve.service';

const partenaireZoneRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/partenaire-zone').then(m => m.PartenaireZone),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/partenaire-zone-detail').then(m => m.PartenaireZoneDetail),
    resolve: {
      partenaireZone: PartenaireZoneResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/partenaire-zone-update').then(m => m.PartenaireZoneUpdate),
    resolve: {
      partenaireZone: PartenaireZoneResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/partenaire-zone-update').then(m => m.PartenaireZoneUpdate),
    resolve: {
      partenaireZone: PartenaireZoneResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partenaireZoneRoute;
