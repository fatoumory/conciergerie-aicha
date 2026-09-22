import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import StatutDemandeResolve from './route/statut-demande-routing-resolve.service';

const statutDemandeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/statut-demande').then(m => m.StatutDemande),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/statut-demande-detail').then(m => m.StatutDemandeDetail),
    resolve: {
      statutDemande: StatutDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/statut-demande-update').then(m => m.StatutDemandeUpdate),
    resolve: {
      statutDemande: StatutDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/statut-demande-update').then(m => m.StatutDemandeUpdate),
    resolve: {
      statutDemande: StatutDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default statutDemandeRoute;
