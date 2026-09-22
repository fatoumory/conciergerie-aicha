package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.CouverturePartenaireTestSamples.*;
import static sn.orange.conciergerie.domain.PartenaireTestSamples.*;
import static sn.orange.conciergerie.domain.ServiceConciergerieTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CouverturePartenaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouverturePartenaire.class);
        CouverturePartenaire couverturePartenaire1 = getCouverturePartenaireSample1();
        CouverturePartenaire couverturePartenaire2 = new CouverturePartenaire();
        assertThat(couverturePartenaire1).isNotEqualTo(couverturePartenaire2);

        couverturePartenaire2.setId(couverturePartenaire1.getId());
        assertThat(couverturePartenaire1).isEqualTo(couverturePartenaire2);

        couverturePartenaire2 = getCouverturePartenaireSample2();
        assertThat(couverturePartenaire1).isNotEqualTo(couverturePartenaire2);
    }

    @Test
    void partenaireTest() {
        CouverturePartenaire couverturePartenaire = getCouverturePartenaireRandomSampleGenerator();
        Partenaire partenaireBack = getPartenaireRandomSampleGenerator();

        couverturePartenaire.setPartenaire(partenaireBack);
        assertThat(couverturePartenaire.getPartenaire()).isEqualTo(partenaireBack);

        couverturePartenaire.partenaire(null);
        assertThat(couverturePartenaire.getPartenaire()).isNull();
    }

    @Test
    void serviceTest() {
        CouverturePartenaire couverturePartenaire = getCouverturePartenaireRandomSampleGenerator();
        ServiceConciergerie serviceConciergerieBack = getServiceConciergerieRandomSampleGenerator();

        couverturePartenaire.setService(serviceConciergerieBack);
        assertThat(couverturePartenaire.getService()).isEqualTo(serviceConciergerieBack);

        couverturePartenaire.service(null);
        assertThat(couverturePartenaire.getService()).isNull();
    }
}
