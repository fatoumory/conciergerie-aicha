package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.PartenaireTestSamples.*;
import static sn.orange.conciergerie.domain.PrestationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class PrestationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prestation.class);
        Prestation prestation1 = getPrestationSample1();
        Prestation prestation2 = new Prestation();
        assertThat(prestation1).isNotEqualTo(prestation2);

        prestation2.setId(prestation1.getId());
        assertThat(prestation1).isEqualTo(prestation2);

        prestation2 = getPrestationSample2();
        assertThat(prestation1).isNotEqualTo(prestation2);
    }

    @Test
    void demandeTest() {
        Prestation prestation = getPrestationRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        prestation.setDemande(demandeBack);
        assertThat(prestation.getDemande()).isEqualTo(demandeBack);

        prestation.demande(null);
        assertThat(prestation.getDemande()).isNull();
    }

    @Test
    void partenaireTest() {
        Prestation prestation = getPrestationRandomSampleGenerator();
        Partenaire partenaireBack = getPartenaireRandomSampleGenerator();

        prestation.setPartenaire(partenaireBack);
        assertThat(prestation.getPartenaire()).isEqualTo(partenaireBack);

        prestation.partenaire(null);
        assertThat(prestation.getPartenaire()).isNull();
    }
}
