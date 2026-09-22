import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import SegmentClientResolve from './route/segment-client-routing-resolve.service';

const segmentClientRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/segment-client').then(m => m.SegmentClient),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/segment-client-detail').then(m => m.SegmentClientDetail),
    resolve: {
      segmentClient: SegmentClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/segment-client-update').then(m => m.SegmentClientUpdate),
    resolve: {
      segmentClient: SegmentClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/segment-client-update').then(m => m.SegmentClientUpdate),
    resolve: {
      segmentClient: SegmentClientResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default segmentClientRoute;
