import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import AffectationDemandeResolve from './route/affectation-demande-routing-resolve.service';

const affectationDemandeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/affectation-demande').then(m => m.AffectationDemande),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/affectation-demande-detail').then(m => m.AffectationDemandeDetail),
    resolve: {
      affectationDemande: AffectationDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/affectation-demande-update').then(m => m.AffectationDemandeUpdate),
    resolve: {
      affectationDemande: AffectationDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/affectation-demande-update').then(m => m.AffectationDemandeUpdate),
    resolve: {
      affectationDemande: AffectationDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default affectationDemandeRoute;
