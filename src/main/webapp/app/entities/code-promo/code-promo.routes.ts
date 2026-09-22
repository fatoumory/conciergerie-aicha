import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import CodePromoResolve from './route/code-promo-routing-resolve.service';

const codePromoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/code-promo').then(m => m.CodePromo),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/code-promo-detail').then(m => m.CodePromoDetail),
    resolve: {
      codePromo: CodePromoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/code-promo-update').then(m => m.CodePromoUpdate),
    resolve: {
      codePromo: CodePromoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/code-promo-update').then(m => m.CodePromoUpdate),
    resolve: {
      codePromo: CodePromoResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default codePromoRoute;
