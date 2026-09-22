import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import ClientResolve from './route/client-routing-resolve.service';

const clientRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/client').then(m => m.Client),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/client-detail').then(m => m.ClientDetail),
    resolve: {
      client: ClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/client-update').then(m => m.ClientUpdate),
    resolve: {
      client: ClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/client-update').then(m => m.ClientUpdate),
    resolve: {
      client: ClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default clientRoute;
