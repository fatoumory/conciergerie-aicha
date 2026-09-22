package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.TypeServiceTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TypeServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeService.class);
        TypeService typeService1 = getTypeServiceSample1();
        TypeService typeService2 = new TypeService();
        assertThat(typeService1).isNotEqualTo(typeService2);

        typeService2.setId(typeService1.getId());
        assertThat(typeService1).isEqualTo(typeService2);

        typeService2 = getTypeServiceSample2();
        assertThat(typeService1).isNotEqualTo(typeService2);
    }
}
