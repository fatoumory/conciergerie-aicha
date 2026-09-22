package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class QuotaServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotaServiceDTO.class);
        QuotaServiceDTO quotaServiceDTO1 = new QuotaServiceDTO();
        quotaServiceDTO1.setId(UUID.randomUUID());
        QuotaServiceDTO quotaServiceDTO2 = new QuotaServiceDTO();
        assertThat(quotaServiceDTO1).isNotEqualTo(quotaServiceDTO2);
        quotaServiceDTO2.setId(quotaServiceDTO1.getId());
        assertThat(quotaServiceDTO1).isEqualTo(quotaServiceDTO2);
        quotaServiceDTO2.setId(UUID.randomUUID());
        assertThat(quotaServiceDTO1).isNotEqualTo(quotaServiceDTO2);
        quotaServiceDTO1.setId(null);
        assertThat(quotaServiceDTO1).isNotEqualTo(quotaServiceDTO2);
    }
}
