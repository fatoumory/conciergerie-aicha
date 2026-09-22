package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class HistoriqueStatutDemandeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueStatutDemandeDTO.class);
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO1 = new HistoriqueStatutDemandeDTO();
        historiqueStatutDemandeDTO1.setId(UUID.randomUUID());
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO2 = new HistoriqueStatutDemandeDTO();
        assertThat(historiqueStatutDemandeDTO1).isNotEqualTo(historiqueStatutDemandeDTO2);
        historiqueStatutDemandeDTO2.setId(historiqueStatutDemandeDTO1.getId());
        assertThat(historiqueStatutDemandeDTO1).isEqualTo(historiqueStatutDemandeDTO2);
        historiqueStatutDemandeDTO2.setId(UUID.randomUUID());
        assertThat(historiqueStatutDemandeDTO1).isNotEqualTo(historiqueStatutDemandeDTO2);
        historiqueStatutDemandeDTO1.setId(null);
        assertThat(historiqueStatutDemandeDTO1).isNotEqualTo(historiqueStatutDemandeDTO2);
    }
}
