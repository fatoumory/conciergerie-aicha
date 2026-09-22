package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.EligibiliteServiceTestSamples.*;
import static sn.orange.conciergerie.domain.SegmentClientTestSamples.*;
import static sn.orange.conciergerie.domain.ServiceConciergerieTestSamples.*;
import static sn.orange.conciergerie.domain.TypeClientTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class EligibiliteServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibiliteService.class);
        EligibiliteService eligibiliteService1 = getEligibiliteServiceSample1();
        EligibiliteService eligibiliteService2 = new EligibiliteService();
        assertThat(eligibiliteService1).isNotEqualTo(eligibiliteService2);

        eligibiliteService2.setId(eligibiliteService1.getId());
        assertThat(eligibiliteService1).isEqualTo(eligibiliteService2);

        eligibiliteService2 = getEligibiliteServiceSample2();
        assertThat(eligibiliteService1).isNotEqualTo(eligibiliteService2);
    }

    @Test
    void serviceTest() {
        EligibiliteService eligibiliteService = getEligibiliteServiceRandomSampleGenerator();
        ServiceConciergerie serviceConciergerieBack = getServiceConciergerieRandomSampleGenerator();

        eligibiliteService.setService(serviceConciergerieBack);
        assertThat(eligibiliteService.getService()).isEqualTo(serviceConciergerieBack);

        eligibiliteService.service(null);
        assertThat(eligibiliteService.getService()).isNull();
    }

    @Test
    void segmentClientTest() {
        EligibiliteService eligibiliteService = getEligibiliteServiceRandomSampleGenerator();
        SegmentClient segmentClientBack = getSegmentClientRandomSampleGenerator();

        eligibiliteService.setSegmentClient(segmentClientBack);
        assertThat(eligibiliteService.getSegmentClient()).isEqualTo(segmentClientBack);

        eligibiliteService.segmentClient(null);
        assertThat(eligibiliteService.getSegmentClient()).isNull();
    }

    @Test
    void typeClientTest() {
        EligibiliteService eligibiliteService = getEligibiliteServiceRandomSampleGenerator();
        TypeClient typeClientBack = getTypeClientRandomSampleGenerator();

        eligibiliteService.setTypeClient(typeClientBack);
        assertThat(eligibiliteService.getTypeClient()).isEqualTo(typeClientBack);

        eligibiliteService.typeClient(null);
        assertThat(eligibiliteService.getTypeClient()).isNull();
    }
}
