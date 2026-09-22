package sn.orange.conciergerie.service.mapper;

import static sn.orange.conciergerie.domain.TypeServiceAsserts.*;
import static sn.orange.conciergerie.domain.TypeServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeServiceMapperTest {

    private TypeServiceMapper typeServiceMapper;

    @BeforeEach
    void setUp() {
        typeServiceMapper = new TypeServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeServiceSample1();
        var actual = typeServiceMapper.toEntity(typeServiceMapper.toDto(expected));
        assertTypeServiceAllPropertiesEquals(expected, actual);
    }
}
