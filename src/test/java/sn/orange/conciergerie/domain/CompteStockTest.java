package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.CompteStockTestSamples.*;
import static sn.orange.conciergerie.domain.ServiceConciergerieTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CompteStockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteStock.class);
        CompteStock compteStock1 = getCompteStockSample1();
        CompteStock compteStock2 = new CompteStock();
        assertThat(compteStock1).isNotEqualTo(compteStock2);

        compteStock2.setId(compteStock1.getId());
        assertThat(compteStock1).isEqualTo(compteStock2);

        compteStock2 = getCompteStockSample2();
        assertThat(compteStock1).isNotEqualTo(compteStock2);
    }

    @Test
    void serviceTest() {
        CompteStock compteStock = getCompteStockRandomSampleGenerator();
        ServiceConciergerie serviceConciergerieBack = getServiceConciergerieRandomSampleGenerator();

        compteStock.setService(serviceConciergerieBack);
        assertThat(compteStock.getService()).isEqualTo(serviceConciergerieBack);

        compteStock.service(null);
        assertThat(compteStock.getService()).isNull();
    }
}
