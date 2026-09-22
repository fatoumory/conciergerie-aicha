import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import FactureResolve from './route/facture-routing-resolve.service';

const factureRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facture').then(m => m.Facture),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facture-detail').then(m => m.FactureDetail),
    resolve: {
      facture: FactureResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facture-update').then(m => m.FactureUpdate),
    resolve: {
      facture: FactureResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facture-update').then(m => m.FactureUpdate),
    resolve: {
      facture: FactureResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default factureRoute;
