package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class PartenaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartenaireDTO.class);
        PartenaireDTO partenaireDTO1 = new PartenaireDTO();
        partenaireDTO1.setId(UUID.randomUUID());
        PartenaireDTO partenaireDTO2 = new PartenaireDTO();
        assertThat(partenaireDTO1).isNotEqualTo(partenaireDTO2);
        partenaireDTO2.setId(partenaireDTO1.getId());
        assertThat(partenaireDTO1).isEqualTo(partenaireDTO2);
        partenaireDTO2.setId(UUID.randomUUID());
        assertThat(partenaireDTO1).isNotEqualTo(partenaireDTO2);
        partenaireDTO1.setId(null);
        assertThat(partenaireDTO1).isNotEqualTo(partenaireDTO2);
    }
}
