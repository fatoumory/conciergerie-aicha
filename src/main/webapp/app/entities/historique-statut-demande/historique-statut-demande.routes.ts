import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import HistoriqueStatutDemandeResolve from './route/historique-statut-demande-routing-resolve.service';

const historiqueStatutDemandeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/historique-statut-demande').then(m => m.HistoriqueStatutDemande),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/historique-statut-demande-detail').then(m => m.HistoriqueStatutDemandeDetail),
    resolve: {
      historiqueStatutDemande: HistoriqueStatutDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/historique-statut-demande-update').then(m => m.HistoriqueStatutDemandeUpdate),
    resolve: {
      historiqueStatutDemande: HistoriqueStatutDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/historique-statut-demande-update').then(m => m.HistoriqueStatutDemandeUpdate),
    resolve: {
      historiqueStatutDemande: HistoriqueStatutDemandeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default historiqueStatutDemandeRoute;
