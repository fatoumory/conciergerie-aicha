import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import CompteStockResolve from './route/compte-stock-routing-resolve.service';

const compteStockRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compte-stock').then(m => m.CompteStock),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compte-stock-detail').then(m => m.CompteStockDetail),
    resolve: {
      compteStock: CompteStockResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compte-stock-update').then(m => m.CompteStockUpdate),
    resolve: {
      compteStock: CompteStockResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compte-stock-update').then(m => m.CompteStockUpdate),
    resolve: {
      compteStock: CompteStockResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default compteStockRoute;
