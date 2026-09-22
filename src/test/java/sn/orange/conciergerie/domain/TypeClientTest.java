package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.TypeClientTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TypeClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeClient.class);
        TypeClient typeClient1 = getTypeClientSample1();
        TypeClient typeClient2 = new TypeClient();
        assertThat(typeClient1).isNotEqualTo(typeClient2);

        typeClient2.setId(typeClient1.getId());
        assertThat(typeClient1).isEqualTo(typeClient2);

        typeClient2 = getTypeClientSample2();
        assertThat(typeClient1).isNotEqualTo(typeClient2);
    }
}
