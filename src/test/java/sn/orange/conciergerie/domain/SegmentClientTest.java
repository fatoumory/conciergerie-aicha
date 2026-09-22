package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.SegmentClientTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class SegmentClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SegmentClient.class);
        SegmentClient segmentClient1 = getSegmentClientSample1();
        SegmentClient segmentClient2 = new SegmentClient();
        assertThat(segmentClient1).isNotEqualTo(segmentClient2);

        segmentClient2.setId(segmentClient1.getId());
        assertThat(segmentClient1).isEqualTo(segmentClient2);

        segmentClient2 = getSegmentClientSample2();
        assertThat(segmentClient1).isNotEqualTo(segmentClient2);
    }
}
