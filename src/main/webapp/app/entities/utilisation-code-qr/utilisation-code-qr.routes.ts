import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import UtilisationCodeQrResolve from './route/utilisation-code-qr-routing-resolve.service';

const utilisationCodeQrRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/utilisation-code-qr').then(m => m.UtilisationCodeQr),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/utilisation-code-qr-detail').then(m => m.UtilisationCodeQrDetail),
    resolve: {
      utilisationCodeQr: UtilisationCodeQrResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/utilisation-code-qr-update').then(m => m.UtilisationCodeQrUpdate),
    resolve: {
      utilisationCodeQr: UtilisationCodeQrResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/utilisation-code-qr-update').then(m => m.UtilisationCodeQrUpdate),
    resolve: {
      utilisationCodeQr: UtilisationCodeQrResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default utilisationCodeQrRoute;
