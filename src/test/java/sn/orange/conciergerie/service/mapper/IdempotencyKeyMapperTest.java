package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.IdempotencyKeyAsserts.*;
import static sn.orange.conciergerie.domain.IdempotencyKeyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdempotencyKeyMapperTest {

    private IdempotencyKeyMapper idempotencyKeyMapper;

    @BeforeEach
    void setUp() {
        idempotencyKeyMapper = new IdempotencyKeyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIdempotencyKeySample1();
        var actual = idempotencyKeyMapper.toEntity(idempotencyKeyMapper.toDto(expected));
        assertIdempotencyKeyAllPropertiesEquals(expected, actual);
    }
}
