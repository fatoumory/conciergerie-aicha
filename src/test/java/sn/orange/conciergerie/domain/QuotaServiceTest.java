package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.EligibiliteServiceTestSamples.*;
import static sn.orange.conciergerie.domain.QuotaServiceTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class QuotaServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotaService.class);
        QuotaService quotaService1 = getQuotaServiceSample1();
        QuotaService quotaService2 = new QuotaService();
        assertThat(quotaService1).isNotEqualTo(quotaService2);

        quotaService2.setId(quotaService1.getId());
        assertThat(quotaService1).isEqualTo(quotaService2);

        quotaService2 = getQuotaServiceSample2();
        assertThat(quotaService1).isNotEqualTo(quotaService2);
    }

    @Test
    void eligibiliteServiceTest() {
        QuotaService quotaService = getQuotaServiceRandomSampleGenerator();
        EligibiliteService eligibiliteServiceBack = getEligibiliteServiceRandomSampleGenerator();

        quotaService.setEligibiliteService(eligibiliteServiceBack);
        assertThat(quotaService.getEligibiliteService()).isEqualTo(eligibiliteServiceBack);

        quotaService.eligibiliteService(null);
        assertThat(quotaService.getEligibiliteService()).isNull();
    }
}
