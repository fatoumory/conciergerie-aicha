package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.ClientTestSamples.*;
import static sn.orange.conciergerie.domain.ConsommationQuotaTestSamples.*;
import static sn.orange.conciergerie.domain.QuotaDetailTestSamples.*;
import static sn.orange.conciergerie.domain.QuotaServiceTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class ConsommationQuotaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsommationQuota.class);
        ConsommationQuota consommationQuota1 = getConsommationQuotaSample1();
        ConsommationQuota consommationQuota2 = new ConsommationQuota();
        assertThat(consommationQuota1).isNotEqualTo(consommationQuota2);

        consommationQuota2.setId(consommationQuota1.getId());
        assertThat(consommationQuota1).isEqualTo(consommationQuota2);

        consommationQuota2 = getConsommationQuotaSample2();
        assertThat(consommationQuota1).isNotEqualTo(consommationQuota2);
    }

    @Test
    void clientTest() {
        ConsommationQuota consommationQuota = getConsommationQuotaRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        consommationQuota.setClient(clientBack);
        assertThat(consommationQuota.getClient()).isEqualTo(clientBack);

        consommationQuota.client(null);
        assertThat(consommationQuota.getClient()).isNull();
    }

    @Test
    void quotaServiceTest() {
        ConsommationQuota consommationQuota = getConsommationQuotaRandomSampleGenerator();
        QuotaService quotaServiceBack = getQuotaServiceRandomSampleGenerator();

        consommationQuota.setQuotaService(quotaServiceBack);
        assertThat(consommationQuota.getQuotaService()).isEqualTo(quotaServiceBack);

        consommationQuota.quotaService(null);
        assertThat(consommationQuota.getQuotaService()).isNull();
    }

    @Test
    void quotaDetailTest() {
        ConsommationQuota consommationQuota = getConsommationQuotaRandomSampleGenerator();
        QuotaDetail quotaDetailBack = getQuotaDetailRandomSampleGenerator();

        consommationQuota.setQuotaDetail(quotaDetailBack);
        assertThat(consommationQuota.getQuotaDetail()).isEqualTo(quotaDetailBack);

        consommationQuota.quotaDetail(null);
        assertThat(consommationQuota.getQuotaDetail()).isNull();
    }
}
