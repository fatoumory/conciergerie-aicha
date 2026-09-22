import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import TransactionPaiementResolve from './route/transaction-paiement-routing-resolve.service';

const transactionPaiementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transaction-paiement').then(m => m.TransactionPaiement),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transaction-paiement-detail').then(m => m.TransactionPaiementDetail),
    resolve: {
      transactionPaiement: TransactionPaiementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transaction-paiement-update').then(m => m.TransactionPaiementUpdate),
    resolve: {
      transactionPaiement: TransactionPaiementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transaction-paiement-update').then(m => m.TransactionPaiementUpdate),
    resolve: {
      transactionPaiement: TransactionPaiementResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default transactionPaiementRoute;
