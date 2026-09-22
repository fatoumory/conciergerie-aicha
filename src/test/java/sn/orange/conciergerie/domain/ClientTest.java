package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.ClientTestSamples.*;
import static sn.orange.conciergerie.domain.SegmentClientTestSamples.*;
import static sn.orange.conciergerie.domain.TypeClientTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void typeClientTest() {
        Client client = getClientRandomSampleGenerator();
        TypeClient typeClientBack = getTypeClientRandomSampleGenerator();

        client.setTypeClient(typeClientBack);
        assertThat(client.getTypeClient()).isEqualTo(typeClientBack);

        client.typeClient(null);
        assertThat(client.getTypeClient()).isNull();
    }

    @Test
    void segmentClientTest() {
        Client client = getClientRandomSampleGenerator();
        SegmentClient segmentClientBack = getSegmentClientRandomSampleGenerator();

        client.setSegmentClient(segmentClientBack);
        assertThat(client.getSegmentClient()).isEqualTo(segmentClientBack);

        client.segmentClient(null);
        assertThat(client.getSegmentClient()).isNull();
    }
}
