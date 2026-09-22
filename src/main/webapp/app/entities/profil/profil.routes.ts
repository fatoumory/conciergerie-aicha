import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import ProfilResolve from './route/profil-routing-resolve.service';

const profilRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/profil').then(m => m.Profil),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/profil-detail').then(m => m.ProfilDetail),
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/profil-update').then(m => m.ProfilUpdate),
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/profil-update').then(m => m.ProfilUpdate),
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default profilRoute;
