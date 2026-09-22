import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import TypeServiceResolve from './route/type-service-routing-resolve.service';

const typeServiceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/type-service').then(m => m.TypeService),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/type-service-detail').then(m => m.TypeServiceDetail),
    resolve: {
      typeService: TypeServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/type-service-update').then(m => m.TypeServiceUpdate),
    resolve: {
      typeService: TypeServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/type-service-update').then(m => m.TypeServiceUpdate),
    resolve: {
      typeService: TypeServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default typeServiceRoute;
