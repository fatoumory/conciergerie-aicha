import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import MouvementStockResolve from './route/mouvement-stock-routing-resolve.service';

const mouvementStockRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/mouvement-stock').then(m => m.MouvementStock),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/mouvement-stock-detail').then(m => m.MouvementStockDetail),
    resolve: {
      mouvementStock: MouvementStockResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/mouvement-stock-update').then(m => m.MouvementStockUpdate),
    resolve: {
      mouvementStock: MouvementStockResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/mouvement-stock-update').then(m => m.MouvementStockUpdate),
    resolve: {
      mouvementStock: MouvementStockResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default mouvementStockRoute;
