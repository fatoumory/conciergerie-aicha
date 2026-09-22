package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class ConsommationQuotaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsommationQuotaDTO.class);
        ConsommationQuotaDTO consommationQuotaDTO1 = new ConsommationQuotaDTO();
        consommationQuotaDTO1.setId(UUID.randomUUID());
        ConsommationQuotaDTO consommationQuotaDTO2 = new ConsommationQuotaDTO();
        assertThat(consommationQuotaDTO1).isNotEqualTo(consommationQuotaDTO2);
        consommationQuotaDTO2.setId(consommationQuotaDTO1.getId());
        assertThat(consommationQuotaDTO1).isEqualTo(consommationQuotaDTO2);
        consommationQuotaDTO2.setId(UUID.randomUUID());
        assertThat(consommationQuotaDTO1).isNotEqualTo(consommationQuotaDTO2);
        consommationQuotaDTO1.setId(null);
        assertThat(consommationQuotaDTO1).isNotEqualTo(consommationQuotaDTO2);
    }
}
