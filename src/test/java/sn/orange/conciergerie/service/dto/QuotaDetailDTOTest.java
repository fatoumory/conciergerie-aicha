package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class QuotaDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotaDetailDTO.class);
        QuotaDetailDTO quotaDetailDTO1 = new QuotaDetailDTO();
        quotaDetailDTO1.setId(UUID.randomUUID());
        QuotaDetailDTO quotaDetailDTO2 = new QuotaDetailDTO();
        assertThat(quotaDetailDTO1).isNotEqualTo(quotaDetailDTO2);
        quotaDetailDTO2.setId(quotaDetailDTO1.getId());
        assertThat(quotaDetailDTO1).isEqualTo(quotaDetailDTO2);
        quotaDetailDTO2.setId(UUID.randomUUID());
        assertThat(quotaDetailDTO1).isNotEqualTo(quotaDetailDTO2);
        quotaDetailDTO1.setId(null);
        assertThat(quotaDetailDTO1).isNotEqualTo(quotaDetailDTO2);
    }
}
