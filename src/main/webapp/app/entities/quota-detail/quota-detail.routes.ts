import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import QuotaDetailResolve from './route/quota-detail-routing-resolve.service';

const quotaDetailRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/quota-detail').then(m => m.QuotaDetail),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/quota-detail-detail').then(m => m.QuotaDetailDetail),
    resolve: {
      quotaDetail: QuotaDetailResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/quota-detail-update').then(m => m.QuotaDetailUpdate),
    resolve: {
      quotaDetail: QuotaDetailResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/quota-detail-update').then(m => m.QuotaDetailUpdate),
    resolve: {
      quotaDetail: QuotaDetailResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default quotaDetailRoute;
