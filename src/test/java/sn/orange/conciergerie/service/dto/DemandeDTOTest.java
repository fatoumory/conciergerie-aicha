package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class DemandeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeDTO.class);
        DemandeDTO demandeDTO1 = new DemandeDTO();
        demandeDTO1.setId(UUID.randomUUID());
        DemandeDTO demandeDTO2 = new DemandeDTO();
        assertThat(demandeDTO1).isNotEqualTo(demandeDTO2);
        demandeDTO2.setId(demandeDTO1.getId());
        assertThat(demandeDTO1).isEqualTo(demandeDTO2);
        demandeDTO2.setId(UUID.randomUUID());
        assertThat(demandeDTO1).isNotEqualTo(demandeDTO2);
        demandeDTO1.setId(null);
        assertThat(demandeDTO1).isNotEqualTo(demandeDTO2);
    }
}
