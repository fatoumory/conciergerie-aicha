import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import CouverturePartenaireResolve from './route/couverture-partenaire-routing-resolve.service';

const couverturePartenaireRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/couverture-partenaire').then(m => m.CouverturePartenaire),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/couverture-partenaire-detail').then(m => m.CouverturePartenaireDetail),
    resolve: {
      couverturePartenaire: CouverturePartenaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/couverture-partenaire-update').then(m => m.CouverturePartenaireUpdate),
    resolve: {
      couverturePartenaire: CouverturePartenaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/couverture-partenaire-update').then(m => m.CouverturePartenaireUpdate),
    resolve: {
      couverturePartenaire: CouverturePartenaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default couverturePartenaireRoute;
