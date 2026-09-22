package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.QuotaDetailTestSamples.*;
import static sn.orange.conciergerie.domain.QuotaServiceTestSamples.*;
import static sn.orange.conciergerie.domain.ZoneTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class QuotaDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotaDetail.class);
        QuotaDetail quotaDetail1 = getQuotaDetailSample1();
        QuotaDetail quotaDetail2 = new QuotaDetail();
        assertThat(quotaDetail1).isNotEqualTo(quotaDetail2);

        quotaDetail2.setId(quotaDetail1.getId());
        assertThat(quotaDetail1).isEqualTo(quotaDetail2);

        quotaDetail2 = getQuotaDetailSample2();
        assertThat(quotaDetail1).isNotEqualTo(quotaDetail2);
    }

    @Test
    void quotaServiceTest() {
        QuotaDetail quotaDetail = getQuotaDetailRandomSampleGenerator();
        QuotaService quotaServiceBack = getQuotaServiceRandomSampleGenerator();

        quotaDetail.setQuotaService(quotaServiceBack);
        assertThat(quotaDetail.getQuotaService()).isEqualTo(quotaServiceBack);

        quotaDetail.quotaService(null);
        assertThat(quotaDetail.getQuotaService()).isNull();
    }

    @Test
    void zoneTest() {
        QuotaDetail quotaDetail = getQuotaDetailRandomSampleGenerator();
        Zone zoneBack = getZoneRandomSampleGenerator();

        quotaDetail.setZone(zoneBack);
        assertThat(quotaDetail.getZone()).isEqualTo(zoneBack);

        quotaDetail.zone(null);
        assertThat(quotaDetail.getZone()).isNull();
    }
}
