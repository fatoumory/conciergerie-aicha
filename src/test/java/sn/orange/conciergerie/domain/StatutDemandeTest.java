package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.StatutDemandeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class StatutDemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatutDemande.class);
        StatutDemande statutDemande1 = getStatutDemandeSample1();
        StatutDemande statutDemande2 = new StatutDemande();
        assertThat(statutDemande1).isNotEqualTo(statutDemande2);

        statutDemande2.setId(statutDemande1.getId());
        assertThat(statutDemande1).isEqualTo(statutDemande2);

        statutDemande2 = getStatutDemandeSample2();
        assertThat(statutDemande1).isNotEqualTo(statutDemande2);
    }
}
