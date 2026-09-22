import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import DemandeResolve from './route/demande-routing-resolve.service';

const demandeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/demande').then(m => m.Demande),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/demande-detail').then(m => m.DemandeDetail),
    resolve: {
      demande: DemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/demande-update').then(m => m.DemandeUpdate),
    resolve: {
      demande: DemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/demande-update').then(m => m.DemandeUpdate),
    resolve: {
      demande: DemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default demandeRoute;
