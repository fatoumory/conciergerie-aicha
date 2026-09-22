package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class TypeClientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeClientDTO.class);
        TypeClientDTO typeClientDTO1 = new TypeClientDTO();
        typeClientDTO1.setId(UUID.randomUUID());
        TypeClientDTO typeClientDTO2 = new TypeClientDTO();
        assertThat(typeClientDTO1).isNotEqualTo(typeClientDTO2);
        typeClientDTO2.setId(typeClientDTO1.getId());
        assertThat(typeClientDTO1).isEqualTo(typeClientDTO2);
        typeClientDTO2.setId(UUID.randomUUID());
        assertThat(typeClientDTO1).isNotEqualTo(typeClientDTO2);
        typeClientDTO1.setId(null);
        assertThat(typeClientDTO1).isNotEqualTo(typeClientDTO2);
    }
}
