import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import TypeClientResolve from './route/type-client-routing-resolve.service';

const typeClientRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/type-client').then(m => m.TypeClient),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/type-client-detail').then(m => m.TypeClientDetail),
    resolve: {
      typeClient: TypeClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/type-client-update').then(m => m.TypeClientUpdate),
    resolve: {
      typeClient: TypeClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/type-client-update').then(m => m.TypeClientUpdate),
    resolve: {
      typeClient: TypeClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default typeClientRoute;
