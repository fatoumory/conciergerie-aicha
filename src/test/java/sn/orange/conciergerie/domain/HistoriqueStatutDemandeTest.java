package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.HistoriqueStatutDemandeTestSamples.*;
import static sn.orange.conciergerie.domain.StatutDemandeTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class HistoriqueStatutDemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueStatutDemande.class);
        HistoriqueStatutDemande historiqueStatutDemande1 = getHistoriqueStatutDemandeSample1();
        HistoriqueStatutDemande historiqueStatutDemande2 = new HistoriqueStatutDemande();
        assertThat(historiqueStatutDemande1).isNotEqualTo(historiqueStatutDemande2);

        historiqueStatutDemande2.setId(historiqueStatutDemande1.getId());
        assertThat(historiqueStatutDemande1).isEqualTo(historiqueStatutDemande2);

        historiqueStatutDemande2 = getHistoriqueStatutDemandeSample2();
        assertThat(historiqueStatutDemande1).isNotEqualTo(historiqueStatutDemande2);
    }

    @Test
    void demandeTest() {
        HistoriqueStatutDemande historiqueStatutDemande = getHistoriqueStatutDemandeRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        historiqueStatutDemande.setDemande(demandeBack);
        assertThat(historiqueStatutDemande.getDemande()).isEqualTo(demandeBack);

        historiqueStatutDemande.demande(null);
        assertThat(historiqueStatutDemande.getDemande()).isNull();
    }

    @Test
    void statutTest() {
        HistoriqueStatutDemande historiqueStatutDemande = getHistoriqueStatutDemandeRandomSampleGenerator();
        StatutDemande statutDemandeBack = getStatutDemandeRandomSampleGenerator();

        historiqueStatutDemande.setStatut(statutDemandeBack);
        assertThat(historiqueStatutDemande.getStatut()).isEqualTo(statutDemandeBack);

        historiqueStatutDemande.statut(null);
        assertThat(historiqueStatutDemande.getStatut()).isNull();
    }
}
