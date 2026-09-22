package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TypeServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeServiceDTO.class);
        TypeServiceDTO typeServiceDTO1 = new TypeServiceDTO();
        typeServiceDTO1.setId(UUID.randomUUID());
        TypeServiceDTO typeServiceDTO2 = new TypeServiceDTO();
        assertThat(typeServiceDTO1).isNotEqualTo(typeServiceDTO2);
        typeServiceDTO2.setId(typeServiceDTO1.getId());
        assertThat(typeServiceDTO1).isEqualTo(typeServiceDTO2);
        typeServiceDTO2.setId(UUID.randomUUID());
        assertThat(typeServiceDTO1).isNotEqualTo(typeServiceDTO2);
        typeServiceDTO1.setId(null);
        assertThat(typeServiceDTO1).isNotEqualTo(typeServiceDTO2);
    }
}
