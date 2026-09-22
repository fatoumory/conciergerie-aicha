package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class AffectationDemandeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffectationDemandeDTO.class);
        AffectationDemandeDTO affectationDemandeDTO1 = new AffectationDemandeDTO();
        affectationDemandeDTO1.setId(UUID.randomUUID());
        AffectationDemandeDTO affectationDemandeDTO2 = new AffectationDemandeDTO();
        assertThat(affectationDemandeDTO1).isNotEqualTo(affectationDemandeDTO2);
        affectationDemandeDTO2.setId(affectationDemandeDTO1.getId());
        assertThat(affectationDemandeDTO1).isEqualTo(affectationDemandeDTO2);
        affectationDemandeDTO2.setId(UUID.randomUUID());
        assertThat(affectationDemandeDTO1).isNotEqualTo(affectationDemandeDTO2);
        affectationDemandeDTO1.setId(null);
        assertThat(affectationDemandeDTO1).isNotEqualTo(affectationDemandeDTO2);
    }
}
