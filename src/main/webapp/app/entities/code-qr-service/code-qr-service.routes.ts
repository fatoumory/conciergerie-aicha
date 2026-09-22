import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import CodeQrServiceResolve from './route/code-qr-service-routing-resolve.service';

const codeQrServiceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/code-qr-service').then(m => m.CodeQrService),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/code-qr-service-detail').then(m => m.CodeQrServiceDetail),
    resolve: {
      codeQrService: CodeQrServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/code-qr-service-update').then(m => m.CodeQrServiceUpdate),
    resolve: {
      codeQrService: CodeQrServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/code-qr-service-update').then(m => m.CodeQrServiceUpdate),
    resolve: {
      codeQrService: CodeQrServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default codeQrServiceRoute;
