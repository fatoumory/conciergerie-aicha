import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import IdempotencyKeyResolve from './route/idempotency-key-routing-resolve.service';

const idempotencyKeyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/idempotency-key').then(m => m.IdempotencyKey),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/idempotency-key-detail').then(m => m.IdempotencyKeyDetail),
    resolve: {
      idempotencyKey: IdempotencyKeyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/idempotency-key-update').then(m => m.IdempotencyKeyUpdate),
    resolve: {
      idempotencyKey: IdempotencyKeyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/idempotency-key-update').then(m => m.IdempotencyKeyUpdate),
    resolve: {
      idempotencyKey: IdempotencyKeyResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default idempotencyKeyRoute;
