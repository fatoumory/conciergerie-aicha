package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class PrestationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestationDTO.class);
        PrestationDTO prestationDTO1 = new PrestationDTO();
        prestationDTO1.setId(UUID.randomUUID());
        PrestationDTO prestationDTO2 = new PrestationDTO();
        assertThat(prestationDTO1).isNotEqualTo(prestationDTO2);
        prestationDTO2.setId(prestationDTO1.getId());
        assertThat(prestationDTO1).isEqualTo(prestationDTO2);
        prestationDTO2.setId(UUID.randomUUID());
        assertThat(prestationDTO1).isNotEqualTo(prestationDTO2);
        prestationDTO1.setId(null);
        assertThat(prestationDTO1).isNotEqualTo(prestationDTO2);
    }
}
