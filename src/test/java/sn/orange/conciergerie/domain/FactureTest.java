package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.FactureTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class FactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = getFactureSample1();
        Facture facture2 = new Facture();
        assertThat(facture1).isNotEqualTo(facture2);

        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);

        facture2 = getFactureSample2();
        assertThat(facture1).isNotEqualTo(facture2);
    }

    @Test
    void demandeTest() {
        Facture facture = getFactureRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        facture.setDemande(demandeBack);
        assertThat(facture.getDemande()).isEqualTo(demandeBack);

        facture.demande(null);
        assertThat(facture.getDemande()).isNull();
    }
}
