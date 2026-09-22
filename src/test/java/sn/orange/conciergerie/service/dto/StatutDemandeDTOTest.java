package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class StatutDemandeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatutDemandeDTO.class);
        StatutDemandeDTO statutDemandeDTO1 = new StatutDemandeDTO();
        statutDemandeDTO1.setId(UUID.randomUUID());
        StatutDemandeDTO statutDemandeDTO2 = new StatutDemandeDTO();
        assertThat(statutDemandeDTO1).isNotEqualTo(statutDemandeDTO2);
        statutDemandeDTO2.setId(statutDemandeDTO1.getId());
        assertThat(statutDemandeDTO1).isEqualTo(statutDemandeDTO2);
        statutDemandeDTO2.setId(UUID.randomUUID());
        assertThat(statutDemandeDTO1).isNotEqualTo(statutDemandeDTO2);
        statutDemandeDTO1.setId(null);
        assertThat(statutDemandeDTO1).isNotEqualTo(statutDemandeDTO2);
    }
}
