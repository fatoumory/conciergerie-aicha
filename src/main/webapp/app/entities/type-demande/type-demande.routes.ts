import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import TypeDemandeResolve from './route/type-demande-routing-resolve.service';

const typeDemandeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/type-demande').then(m => m.TypeDemande),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/type-demande-detail').then(m => m.TypeDemandeDetail),
    resolve: {
      typeDemande: TypeDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/type-demande-update').then(m => m.TypeDemandeUpdate),
    resolve: {
      typeDemande: TypeDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/type-demande-update').then(m => m.TypeDemandeUpdate),
    resolve: {
      typeDemande: TypeDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default typeDemandeRoute;
