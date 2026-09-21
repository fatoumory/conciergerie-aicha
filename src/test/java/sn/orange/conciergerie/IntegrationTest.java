package sn.orange.conciergerie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import sn.orange.conciergerie.config.AsyncSyncConfiguration;
import sn.orange.conciergerie.config.DatabaseTestcontainer;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        ConciergerieApp.class,
        AsyncSyncConfiguration.class,
        sn.orange.conciergerie.config.JacksonHibernateConfiguration.class,
        DatabaseTestcontainer.class,
    }
)
public @interface IntegrationTest {}
