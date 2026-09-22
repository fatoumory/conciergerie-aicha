import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import PartenaireResolve from './route/partenaire-routing-resolve.service';

const partenaireRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/partenaire').then(m => m.Partenaire),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/partenaire-detail').then(m => m.PartenaireDetail),
    resolve: {
      partenaire: PartenaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/partenaire-update').then(m => m.PartenaireUpdate),
    resolve: {
      partenaire: PartenaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/partenaire-update').then(m => m.PartenaireUpdate),
    resolve: {
      partenaire: PartenaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default partenaireRoute;
