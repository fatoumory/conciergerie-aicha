import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import EligibiliteServiceResolve from './route/eligibilite-service-routing-resolve.service';

const eligibiliteServiceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/eligibilite-service').then(m => m.EligibiliteService),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/eligibilite-service-detail').then(m => m.EligibiliteServiceDetail),
    resolve: {
      eligibiliteService: EligibiliteServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/eligibilite-service-update').then(m => m.EligibiliteServiceUpdate),
    resolve: {
      eligibiliteService: EligibiliteServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/eligibilite-service-update').then(m => m.EligibiliteServiceUpdate),
    resolve: {
      eligibiliteService: EligibiliteServiceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default eligibiliteServiceRoute;
