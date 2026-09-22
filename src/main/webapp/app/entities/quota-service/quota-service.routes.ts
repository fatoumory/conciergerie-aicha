import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import QuotaServiceResolve from './route/quota-service-routing-resolve.service';

const quotaServiceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quota-service').then(m => m.QuotaService),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quota-service-detail').then(m => m.QuotaServiceDetail),
    resolve: {
      quotaService: QuotaServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quota-service-update').then(m => m.QuotaServiceUpdate),
    resolve: {
      quotaService: QuotaServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quota-service-update').then(m => m.QuotaServiceUpdate),
    resolve: {
      quotaService: QuotaServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quotaServiceRoute;
