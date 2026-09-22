import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import EvaluationResolve from './route/evaluation-routing-resolve.service';

const evaluationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/evaluation').then(m => m.Evaluation),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/evaluation-detail').then(m => m.EvaluationDetail),
    resolve: {
      evaluation: EvaluationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/evaluation-update').then(m => m.EvaluationUpdate),
    resolve: {
      evaluation: EvaluationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/evaluation-update').then(m => m.EvaluationUpdate),
    resolve: {
      evaluation: EvaluationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default evaluationRoute;
