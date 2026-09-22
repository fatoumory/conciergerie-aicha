import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'userManagement.home.title',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'conciergerieApp.adminAuthority.home.title',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'profil',
    title: 'conciergerieApp.profil.home.title',
    loadChildren: () => import('./profil/profil.routes'),
  },
  {
    path: 'client',
    title: 'conciergerieApp.client.home.title',
    loadChildren: () => import('./client/client.routes'),
  },
  {
    path: 'type-client',
    title: 'conciergerieApp.typeClient.home.title',
    loadChildren: () => import('./type-client/type-client.routes'),
  },
  {
    path: 'segment-client',
    title: 'conciergerieApp.segmentClient.home.title',
    loadChildren: () => import('./segment-client/segment-client.routes'),
  },
  {
    path: 'type-service',
    title: 'conciergerieApp.typeService.home.title',
    loadChildren: () => import('./type-service/type-service.routes'),
  },
  {
    path: 'service-conciergerie',
    title: 'conciergerieApp.serviceConciergerie.home.title',
    loadChildren: () => import('./service-conciergerie/service-conciergerie.routes'),
  },
  {
    path: 'eligibilite-service',
    title: 'conciergerieApp.eligibiliteService.home.title',
    loadChildren: () => import('./eligibilite-service/eligibilite-service.routes'),
  },
  {
    path: 'quota-service',
    title: 'conciergerieApp.quotaService.home.title',
    loadChildren: () => import('./quota-service/quota-service.routes'),
  },
  {
    path: 'quota-detail',
    title: 'conciergerieApp.quotaDetail.home.title',
    loadChildren: () => import('./quota-detail/quota-detail.routes'),
  },
  {
    path: 'consommation-quota',
    title: 'conciergerieApp.consommationQuota.home.title',
    loadChildren: () => import('./consommation-quota/consommation-quota.routes'),
  },
  {
    path: 'partenaire',
    title: 'conciergerieApp.partenaire.home.title',
    loadChildren: () => import('./partenaire/partenaire.routes'),
  },
  {
    path: 'zone',
    title: 'conciergerieApp.zone.home.title',
    loadChildren: () => import('./zone/zone.routes'),
  },
  {
    path: 'couverture-partenaire',
    title: 'conciergerieApp.couverturePartenaire.home.title',
    loadChildren: () => import('./couverture-partenaire/couverture-partenaire.routes'),
  },
  {
    path: 'partenaire-zone',
    title: 'conciergerieApp.partenaireZone.home.title',
    loadChildren: () => import('./partenaire-zone/partenaire-zone.routes'),
  },
  {
    path: 'compte-stock',
    title: 'conciergerieApp.compteStock.home.title',
    loadChildren: () => import('./compte-stock/compte-stock.routes'),
  },
  {
    path: 'mouvement-stock',
    title: 'conciergerieApp.mouvementStock.home.title',
    loadChildren: () => import('./mouvement-stock/mouvement-stock.routes'),
  },
  {
    path: 'demande',
    title: 'conciergerieApp.demande.home.title',
    loadChildren: () => import('./demande/demande.routes'),
  },
  {
    path: 'type-demande',
    title: 'conciergerieApp.typeDemande.home.title',
    loadChildren: () => import('./type-demande/type-demande.routes'),
  },
  {
    path: 'statut-demande',
    title: 'conciergerieApp.statutDemande.home.title',
    loadChildren: () => import('./statut-demande/statut-demande.routes'),
  },
  {
    path: 'historique-statut-demande',
    title: 'conciergerieApp.historiqueStatutDemande.home.title',
    loadChildren: () => import('./historique-statut-demande/historique-statut-demande.routes'),
  },
  {
    path: 'affectation-demande',
    title: 'conciergerieApp.affectationDemande.home.title',
    loadChildren: () => import('./affectation-demande/affectation-demande.routes'),
  },
  {
    path: 'prestation',
    title: 'conciergerieApp.prestation.home.title',
    loadChildren: () => import('./prestation/prestation.routes'),
  },
  {
    path: 'code-qr-service',
    title: 'conciergerieApp.codeQrService.home.title',
    loadChildren: () => import('./code-qr-service/code-qr-service.routes'),
  },
  {
    path: 'utilisation-code-qr',
    title: 'conciergerieApp.utilisationCodeQr.home.title',
    loadChildren: () => import('./utilisation-code-qr/utilisation-code-qr.routes'),
  },
  {
    path: 'transaction-paiement',
    title: 'conciergerieApp.transactionPaiement.home.title',
    loadChildren: () => import('./transaction-paiement/transaction-paiement.routes'),
  },
  {
    path: 'facture',
    title: 'conciergerieApp.facture.home.title',
    loadChildren: () => import('./facture/facture.routes'),
  },
  {
    path: 'code-promo',
    title: 'conciergerieApp.codePromo.home.title',
    loadChildren: () => import('./code-promo/code-promo.routes'),
  },
  {
    path: 'notification',
    title: 'conciergerieApp.notification.home.title',
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'evaluation',
    title: 'conciergerieApp.evaluation.home.title',
    loadChildren: () => import('./evaluation/evaluation.routes'),
  },
  {
    path: 'idempotency-key',
    title: 'conciergerieApp.idempotencyKey.home.title',
    loadChildren: () => import('./idempotency-key/idempotency-key.routes'),
  },
  {
    path: 'journal-audit',
    title: 'conciergerieApp.journalAudit.home.title',
    loadChildren: () => import('./journal-audit/journal-audit.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
