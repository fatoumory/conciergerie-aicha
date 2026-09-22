package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.AffectationDemandeTestSamples.*;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.PartenaireTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class AffectationDemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffectationDemande.class);
        AffectationDemande affectationDemande1 = getAffectationDemandeSample1();
        AffectationDemande affectationDemande2 = new AffectationDemande();
        assertThat(affectationDemande1).isNotEqualTo(affectationDemande2);

        affectationDemande2.setId(affectationDemande1.getId());
        assertThat(affectationDemande1).isEqualTo(affectationDemande2);

        affectationDemande2 = getAffectationDemandeSample2();
        assertThat(affectationDemande1).isNotEqualTo(affectationDemande2);
    }

    @Test
    void demandeTest() {
        AffectationDemande affectationDemande = getAffectationDemandeRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        affectationDemande.setDemande(demandeBack);
        assertThat(affectationDemande.getDemande()).isEqualTo(demandeBack);

        affectationDemande.demande(null);
        assertThat(affectationDemande.getDemande()).isNull();
    }

    @Test
    void partenaireTest() {
        AffectationDemande affectationDemande = getAffectationDemandeRandomSampleGenerator();
        Partenaire partenaireBack = getPartenaireRandomSampleGenerator();

        affectationDemande.setPartenaire(partenaireBack);
        assertThat(affectationDemande.getPartenaire()).isEqualTo(partenaireBack);

        affectationDemande.partenaire(null);
        assertThat(affectationDemande.getPartenaire()).isNull();
    }
}
