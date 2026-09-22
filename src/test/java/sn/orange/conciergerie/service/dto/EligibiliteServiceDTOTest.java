package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class EligibiliteServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibiliteServiceDTO.class);
        EligibiliteServiceDTO eligibiliteServiceDTO1 = new EligibiliteServiceDTO();
        eligibiliteServiceDTO1.setId(UUID.randomUUID());
        EligibiliteServiceDTO eligibiliteServiceDTO2 = new EligibiliteServiceDTO();
        assertThat(eligibiliteServiceDTO1).isNotEqualTo(eligibiliteServiceDTO2);
        eligibiliteServiceDTO2.setId(eligibiliteServiceDTO1.getId());
        assertThat(eligibiliteServiceDTO1).isEqualTo(eligibiliteServiceDTO2);
        eligibiliteServiceDTO2.setId(UUID.randomUUID());
        assertThat(eligibiliteServiceDTO1).isNotEqualTo(eligibiliteServiceDTO2);
        eligibiliteServiceDTO1.setId(null);
        assertThat(eligibiliteServiceDTO1).isNotEqualTo(eligibiliteServiceDTO2);
    }
}
