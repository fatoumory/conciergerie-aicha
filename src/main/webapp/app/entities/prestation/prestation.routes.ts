import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import PrestationResolve from './route/prestation-routing-resolve.service';

const prestationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/prestation').then(m => m.Prestation),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/prestation-detail').then(m => m.PrestationDetail),
    resolve: {
      prestation: PrestationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/prestation-update').then(m => m.PrestationUpdate),
    resolve: {
      prestation: PrestationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/prestation-update').then(m => m.PrestationUpdate),
    resolve: {
      prestation: PrestationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default prestationRoute;
