package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.CompteStockTestSamples.*;
import static sn.orange.conciergerie.domain.ServiceConciergerieTestSamples.*;
import static sn.orange.conciergerie.domain.TypeServiceTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class ServiceConciergerieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceConciergerie.class);
        ServiceConciergerie serviceConciergerie1 = getServiceConciergerieSample1();
        ServiceConciergerie serviceConciergerie2 = new ServiceConciergerie();
        assertThat(serviceConciergerie1).isNotEqualTo(serviceConciergerie2);

        serviceConciergerie2.setId(serviceConciergerie1.getId());
        assertThat(serviceConciergerie1).isEqualTo(serviceConciergerie2);

        serviceConciergerie2 = getServiceConciergerieSample2();
        assertThat(serviceConciergerie1).isNotEqualTo(serviceConciergerie2);
    }

    @Test
    void typeServiceTest() {
        ServiceConciergerie serviceConciergerie = getServiceConciergerieRandomSampleGenerator();
        TypeService typeServiceBack = getTypeServiceRandomSampleGenerator();

        serviceConciergerie.setTypeService(typeServiceBack);
        assertThat(serviceConciergerie.getTypeService()).isEqualTo(typeServiceBack);

        serviceConciergerie.typeService(null);
        assertThat(serviceConciergerie.getTypeService()).isNull();
    }

    @Test
    void compteStockTest() {
        ServiceConciergerie serviceConciergerie = getServiceConciergerieRandomSampleGenerator();
        CompteStock compteStockBack = getCompteStockRandomSampleGenerator();

        serviceConciergerie.setCompteStock(compteStockBack);
        assertThat(serviceConciergerie.getCompteStock()).isEqualTo(compteStockBack);
        assertThat(compteStockBack.getService()).isEqualTo(serviceConciergerie);

        serviceConciergerie.compteStock(null);
        assertThat(serviceConciergerie.getCompteStock()).isNull();
        assertThat(compteStockBack.getService()).isNull();
    }
}
