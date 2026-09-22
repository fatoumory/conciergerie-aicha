package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TypeDemandeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDemandeDTO.class);
        TypeDemandeDTO typeDemandeDTO1 = new TypeDemandeDTO();
        typeDemandeDTO1.setId(UUID.randomUUID());
        TypeDemandeDTO typeDemandeDTO2 = new TypeDemandeDTO();
        assertThat(typeDemandeDTO1).isNotEqualTo(typeDemandeDTO2);
        typeDemandeDTO2.setId(typeDemandeDTO1.getId());
        assertThat(typeDemandeDTO1).isEqualTo(typeDemandeDTO2);
        typeDemandeDTO2.setId(UUID.randomUUID());
        assertThat(typeDemandeDTO1).isNotEqualTo(typeDemandeDTO2);
        typeDemandeDTO1.setId(null);
        assertThat(typeDemandeDTO1).isNotEqualTo(typeDemandeDTO2);
    }
}
