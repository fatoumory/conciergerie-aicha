package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.IdempotencyKeyTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class IdempotencyKeyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdempotencyKey.class);
        IdempotencyKey idempotencyKey1 = getIdempotencyKeySample1();
        IdempotencyKey idempotencyKey2 = new IdempotencyKey();
        assertThat(idempotencyKey1).isNotEqualTo(idempotencyKey2);

        idempotencyKey2.setId(idempotencyKey1.getId());
        assertThat(idempotencyKey1).isEqualTo(idempotencyKey2);

        idempotencyKey2 = getIdempotencyKeySample2();
        assertThat(idempotencyKey1).isNotEqualTo(idempotencyKey2);
    }
}
