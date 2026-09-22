package sn.orange.conciergerie.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        var ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, sn.orange.conciergerie.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, sn.orange.conciergerie.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, sn.orange.conciergerie.domain.User.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Authority.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.User.class.getName() + ".authorities");
            createCache(cm, sn.orange.conciergerie.domain.Profil.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Profil.class.getName() + ".utilisateurs");
            createCache(cm, sn.orange.conciergerie.domain.Profil.class.getName() + ".roles");
            createCache(cm, sn.orange.conciergerie.domain.Client.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.TypeClient.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.SegmentClient.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.TypeService.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.ServiceConciergerie.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.EligibiliteService.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.QuotaService.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.QuotaDetail.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.ConsommationQuota.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Partenaire.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Zone.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.CouverturePartenaire.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.PartenaireZone.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.CompteStock.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.MouvementStock.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Demande.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.TypeDemande.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.StatutDemande.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.HistoriqueStatutDemande.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.AffectationDemande.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Prestation.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.CodeQrService.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.UtilisationCodeQr.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.TransactionPaiement.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Facture.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.CodePromo.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Notification.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.Evaluation.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.IdempotencyKey.class.getName());
            createCache(cm, sn.orange.conciergerie.domain.JournalAudit.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
