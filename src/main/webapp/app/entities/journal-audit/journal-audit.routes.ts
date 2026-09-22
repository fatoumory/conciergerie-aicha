import { Routes } from '@angular/router';

import { userRouteAccessService } from 'app/core/auth';

import JournalAuditResolve from './route/journal-audit-routing-resolve.service';

const journalAuditRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/journal-audit').then(m => m.JournalAudit),
    data: {},
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/journal-audit-detail').then(m => m.JournalAuditDetail),
    resolve: {
      journalAudit: JournalAuditResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/journal-audit-update').then(m => m.JournalAuditUpdate),
    resolve: {
      journalAudit: JournalAuditResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/journal-audit-update').then(m => m.JournalAuditUpdate),
    resolve: {
      journalAudit: JournalAuditResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default journalAuditRoute;
