import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import ConsommationQuotaResolve from './route/consommation-quota-routing-resolve.service';

const consommationQuotaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/consommation-quota').then(m => m.ConsommationQuota),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/consommation-quota-detail').then(m => m.ConsommationQuotaDetail),
    resolve: {
      consommationQuota: ConsommationQuotaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/consommation-quota-update').then(m => m.ConsommationQuotaUpdate),
    resolve: {
      consommationQuota: ConsommationQuotaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/consommation-quota-update').then(m => m.ConsommationQuotaUpdate),
    resolve: {
      consommationQuota: ConsommationQuotaResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default consommationQuotaRoute;
