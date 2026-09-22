package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.PartenaireTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class PartenaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Partenaire.class);
        Partenaire partenaire1 = getPartenaireSample1();
        Partenaire partenaire2 = new Partenaire();
        assertThat(partenaire1).isNotEqualTo(partenaire2);

        partenaire2.setId(partenaire1.getId());
        assertThat(partenaire1).isEqualTo(partenaire2);

        partenaire2 = getPartenaireSample2();
        assertThat(partenaire1).isNotEqualTo(partenaire2);
    }
}
