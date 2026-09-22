package sn.orange.conciergerie.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class IdempotencyKeyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdempotencyKeyDTO.class);
        IdempotencyKeyDTO idempotencyKeyDTO1 = new IdempotencyKeyDTO();
        idempotencyKeyDTO1.setId(UUID.randomUUID());
        IdempotencyKeyDTO idempotencyKeyDTO2 = new IdempotencyKeyDTO();
        assertThat(idempotencyKeyDTO1).isNotEqualTo(idempotencyKeyDTO2);
        idempotencyKeyDTO2.setId(idempotencyKeyDTO1.getId());
        assertThat(idempotencyKeyDTO1).isEqualTo(idempotencyKeyDTO2);
        idempotencyKeyDTO2.setId(UUID.randomUUID());
        assertThat(idempotencyKeyDTO1).isNotEqualTo(idempotencyKeyDTO2);
        idempotencyKeyDTO1.setId(null);
        assertThat(idempotencyKeyDTO1).isNotEqualTo(idempotencyKeyDTO2);
    }
}
