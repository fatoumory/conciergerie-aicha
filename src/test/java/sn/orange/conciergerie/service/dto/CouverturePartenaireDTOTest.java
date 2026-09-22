package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class CouverturePartenaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouverturePartenaireDTO.class);
        CouverturePartenaireDTO couverturePartenaireDTO1 = new CouverturePartenaireDTO();
        couverturePartenaireDTO1.setId(UUID.randomUUID());
        CouverturePartenaireDTO couverturePartenaireDTO2 = new CouverturePartenaireDTO();
        assertThat(couverturePartenaireDTO1).isNotEqualTo(couverturePartenaireDTO2);
        couverturePartenaireDTO2.setId(couverturePartenaireDTO1.getId());
        assertThat(couverturePartenaireDTO1).isEqualTo(couverturePartenaireDTO2);
        couverturePartenaireDTO2.setId(UUID.randomUUID());
        assertThat(couverturePartenaireDTO1).isNotEqualTo(couverturePartenaireDTO2);
        couverturePartenaireDTO1.setId(null);
        assertThat(couverturePartenaireDTO1).isNotEqualTo(couverturePartenaireDTO2);
    }
}
