import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import ServiceConciergerieResolve from './route/service-conciergerie-routing-resolve.service';

const serviceConciergerieRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-conciergerie').then(m => m.ServiceConciergerie),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-conciergerie-detail').then(m => m.ServiceConciergerieDetail),
    resolve: {
      serviceConciergerie: ServiceConciergerieResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-conciergerie-update').then(m => m.ServiceConciergerieUpdate),
    resolve: {
      serviceConciergerie: ServiceConciergerieResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-conciergerie-update').then(m => m.ServiceConciergerieUpdate),
    resolve: {
      serviceConciergerie: ServiceConciergerieResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default serviceConciergerieRoute;
