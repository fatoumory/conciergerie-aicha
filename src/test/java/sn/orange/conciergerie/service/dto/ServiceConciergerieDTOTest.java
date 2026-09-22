package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class ServiceConciergerieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceConciergerieDTO.class);
        ServiceConciergerieDTO serviceConciergerieDTO1 = new ServiceConciergerieDTO();
        serviceConciergerieDTO1.setId(UUID.randomUUID());
        ServiceConciergerieDTO serviceConciergerieDTO2 = new ServiceConciergerieDTO();
        assertThat(serviceConciergerieDTO1).isNotEqualTo(serviceConciergerieDTO2);
        serviceConciergerieDTO2.setId(serviceConciergerieDTO1.getId());
        assertThat(serviceConciergerieDTO1).isEqualTo(serviceConciergerieDTO2);
        serviceConciergerieDTO2.setId(UUID.randomUUID());
        assertThat(serviceConciergerieDTO1).isNotEqualTo(serviceConciergerieDTO2);
        serviceConciergerieDTO1.setId(null);
        assertThat(serviceConciergerieDTO1).isNotEqualTo(serviceConciergerieDTO2);
    }
}
